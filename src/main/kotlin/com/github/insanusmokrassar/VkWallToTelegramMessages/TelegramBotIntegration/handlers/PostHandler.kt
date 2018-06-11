package com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration.handlers

import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.Post
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.Attachment
import com.pengrad.telegrambot.request.BaseRequest

val handlersOrder = linkedSetOf<PostHandler>(
    MediaGroupHandler()
)

typealias PostHandler = (
    chatId: Long,
    post: Post,
    leftAttachments: MutableList<Attachment>
) -> BaseRequest<*, *>?