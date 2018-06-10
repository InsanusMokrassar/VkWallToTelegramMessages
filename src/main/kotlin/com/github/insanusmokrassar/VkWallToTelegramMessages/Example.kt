package com.github.insanusmokrassar.VkWallToTelegramMessages

import com.github.insanusmokrassar.IObjectKRealisations.toIObject
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.VKIntegrationHolder
import kotlinx.coroutines.experimental.runBlocking

fun main(args: Array<String>) {
    VKIntegrationHolder(args[0]).apply {
        runBlocking {
            wall.get(
                "hose_socks"
            ).await().let {
                println(it)
                it.response.items.forEach {
                    println(it.toIObject())
                }
            }
        }
    }
}
