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

    private var job: Job = launch {
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
                    val filtered = originalList.filter {
                        it.dateInMillis > settings.lastReadDate
                    }
                    filtered.forEach {
                        it.adaptedAttachments.forEach {
                            it.prepareAttachment(methodsHolder)
                        }
                    }
                    posts.addAll(
                        filtered.sortedBy { it.date }
                    )
                    if (originalList.size > filtered.size) {
                        break
                    } else {
                        offset += originalList.size
                    }
                }
                if (posts.isNotEmpty()) {
                    posts.forEach {
                        action(it)
                        successPost = it
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                delay(config.updateDelay, TimeUnit.MILLISECONDS)
                successPost ?. dateInMillis ?.let {
                    db.settings = Settings(it)
                }
            }
        }
    }
}
