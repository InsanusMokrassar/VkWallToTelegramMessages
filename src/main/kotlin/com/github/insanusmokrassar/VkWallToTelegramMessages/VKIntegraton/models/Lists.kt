package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models

open class Lists<T>(
    val count: Long,
    val items: List<T> = emptyList()
)
