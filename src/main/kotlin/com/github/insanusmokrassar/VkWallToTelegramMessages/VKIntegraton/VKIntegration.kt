package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton

import com.github.insanusmokrassar.VkWallToTelegramMessages.*
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.Post
import kotlinx.coroutines.experimental.*
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

typealias NewPostCallback = suspend (Post) -> Unit

private const val VKIntegrationTag = "Getting updates"

class VKIntegration(
    config: Config,
    action: NewPostCallback,
    private val db: DB
) {
    private val methodsHolder = VKMethodsHolder(config.accessToken)

    private val logger = LoggerFactory.getLogger(VKIntegrationTag)

    private var job = launch {
        while (true) {
            var successPost: Post? = null
            try {
                val settings = db.settings
                val posts = mutableListOf<Post>()
                var offset = 0
                while (true) {
                    val originalList = (
                        config.wallOwnerId?.let {
                            methodsHolder.wall.get(
                                it,
                                offset
                            )
                        } ?: methodsHolder.wall.get(
                            config.wallDomain,
                            offset
                        )
                        ).await().response.items
                    logger.info("Request list count: ${originalList.size}")
                    if (originalList.isEmpty()) {
                        break
                    }
                    val prepared = originalList.filter {
                        it.date > settings.lastReadDateSeconds
                    }
                    logger.info("Prepared list count: ${prepared.size}")
                    if (prepared.isEmpty()) {
                        break
                    }
                    prepared.forEach {
                        it.prepareAttachments(methodsHolder)
                    }
                    posts.addAll(
                        prepared
                    )
                    if (originalList.size > prepared.size) {
                        break
                    } else {
                        offset += originalList.size
                    }
                }
                logger.info("Prepared list count: ${posts.size}")
                if (posts.isNotEmpty()) {
                    posts.sortedBy {
                        it.date
                    }.forEach {
                        action(it)
                        successPost = it
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                logger.info("Last published post date: ${successPost ?. dateInMillis ?: "unknown"}")
                successPost ?. dateInMillis ?.let {
                    db.settings = Settings(it)
                }
                delay(config.updateDelay, TimeUnit.MILLISECONDS)
            }
        }
    }
}
