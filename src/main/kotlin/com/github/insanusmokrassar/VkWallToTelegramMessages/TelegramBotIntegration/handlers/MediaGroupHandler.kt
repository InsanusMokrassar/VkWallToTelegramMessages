package com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration.handlers

import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.Post
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.Attachment
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.photo.PhotoAttachment
import com.pengrad.telegrambot.model.request.InputMediaPhoto
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMediaGroup

class MediaGroupHandler : PostHandler {
    override fun invoke(
        chatId: Long,
        post: Post,
        leftAttachments: MutableList<Attachment>
    ): BaseRequest<*, *>? {
        return leftAttachments.filter {
            it is PhotoAttachment
        }.let {
            if (it.size > 1) {
                SendMediaGroup(
                    chatId,
                    *it.mapNotNull {
                        (it as? PhotoAttachment) ?.let {
                            leftAttachments.remove(it)
                            it.photo.biggestSize ?. url ?.let {
                                InputMediaPhoto(it)
                            }
                        }
                    }.toTypedArray()
                )
            } else {
                null
            }
        }
    }
}