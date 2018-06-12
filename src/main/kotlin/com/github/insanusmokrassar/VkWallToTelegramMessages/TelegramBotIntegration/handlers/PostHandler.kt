package com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration.handlers

import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.Post
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.Attachment
import com.pengrad.telegrambot.request.BaseRequest
import org.slf4j.LoggerFactory

val handlersOrder = linkedSetOf(
    LinkHandler(),
    MediaGroupHandler(),
    VideoHandler(),
    PhotoHandler(),
    AudioHandler(),
    DocumentHandler(),
    TextHandler()
)

const val PostHandlerTag = "com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration.handlers.PostHandler"
val postsLogger = LoggerFactory.getLogger(PostHandlerTag)

typealias PostHandler = (
    chatId: Long,
    post: Post,
    leftAttachments: MutableList<Attachment>
) -> List<BaseRequest<*, *>>