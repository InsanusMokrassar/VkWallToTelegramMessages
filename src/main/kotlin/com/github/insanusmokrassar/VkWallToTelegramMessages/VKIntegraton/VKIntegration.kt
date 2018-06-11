package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton

import com.github.insanusmokrassar.VkWallToTelegramMessages.*
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.Post
import kotlinx.coroutines.experimental.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

typealias NewPostsCallback = suspend (List<Post>) -> Unit

class VKIntegration(
    config: Config,
    action: NewPostsCallback,
    private val db: DB
) {
    private val methodsHolder = VKMethodsHolder(config.accessToken)

    private val scheduler = Executors.newScheduledThreadPool(1)

    private var job: Job? = null

    init {
        scheduler.scheduleAtFixedRate(
            {
                job ?: launch {
                    try {
                        val settings = db.settings
                        val posts = mutableListOf<Post>()
                        var offset = 0
                        while (true) {
                            val originalList = (
                                config.wallOwnerId ?.let {
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
                            action(posts)
                        }
                        posts.maxBy { it.date } ?.dateInMillis ?.let {
                            db.settings = Settings(it)
                        }
                    } finally {
                        job = null
                    }
                }.also {
                    job = it
                }
            },
            0,
            config.updateDelay,
            TimeUnit.MILLISECONDS
        )
    }
}
