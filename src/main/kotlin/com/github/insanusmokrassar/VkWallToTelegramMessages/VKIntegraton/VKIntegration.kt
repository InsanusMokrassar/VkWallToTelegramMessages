package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton

import com.github.insanusmokrassar.VkWallToTelegramMessages.*
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.Post
import kotlinx.coroutines.experimental.*
import java.util.concurrent.TimeUnit

typealias NewPostCallback = suspend (Post) -> Unit

class VKIntegration(
    config: Config,
    action: NewPostCallback,
    private val db: DB
) {
    private val methodsHolder = VKMethodsHolder(config.accessToken)

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
                    if (originalList.isEmpty()) {
                        break
                    }
                    val prepared = originalList.filter {
                        it.date > settings.lastReadDateSeconds
                    }
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
                successPost ?. dateInMillis ?.let {
                    db.settings = Settings(it)
                }
                delay(config.updateDelay, TimeUnit.MILLISECONDS)
            }
        }
    }
}
