package com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration.handlers

import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.Post
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.Attachment
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.photo.PhotoAttachment
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.video.VideoAttachment
import com.pengrad.telegrambot.model.request.InputMediaPhoto
import com.pengrad.telegrambot.model.request.InputMediaVideo
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMediaGroup

class MediaGroupHandler : PostHandler {
    override fun invoke(
        chatId: Long,
        post: Post,
        leftAttachments: MutableList<Attachment>
    ): List<BaseRequest<*, *>> {
        return leftAttachments.filter {
            it is PhotoAttachment || (it is VideoAttachment && it.video.player != null)
        }.let {
            if (it.size > 1) {
                listOf(
                    SendMediaGroup(
                        chatId,
                        *it.mapNotNull {
                            leftAttachments.remove(it)
                            (it as? PhotoAttachment) ?.let {
                                it.photo.biggestSize ?. url ?.let {
                                    InputMediaPhoto(it)
                                }
                            } ?: (it as? VideoAttachment) ?.let {
                                InputMediaVideo(it.video.player)
                            }
                        }.toTypedArray()
                    )
                )
            } else {
                emptyList()
            }
        }
    }
}