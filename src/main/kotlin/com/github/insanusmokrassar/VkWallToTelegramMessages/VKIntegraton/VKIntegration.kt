package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton

import com.github.insanusmokrassar.VkWallToTelegramMessages.*
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.Post
import kotlinx.coroutines.experimental.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class VKIntegration(
    config: Config,
    action: (List<Post>) -> Unit,
    private val db: DB
) {
    private val methodsHolder = VKMethodsHolder(config.accessToken)

    private val scheduler = Executors.newScheduledThreadPool(1)

    private var job: Job? = null

    init {
        scheduler.schedule(
            {
                job ?: launch {
                    try {
                        val settings = db.settings
                        val posts = mutableListOf<Post>()
                        while (true) {
                            val originalList = methodsHolder.wall.get(
                                config.wallDomain
                            ).await().response.items
                            val filtered = originalList.filter {
                                it.dateInMillis > settings.lastReadDate
                            }
                            posts.addAll(filtered)
                            if (originalList.size > filtered.size) {
                                break
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
            10,
            TimeUnit.SECONDS
        )
    }
}
