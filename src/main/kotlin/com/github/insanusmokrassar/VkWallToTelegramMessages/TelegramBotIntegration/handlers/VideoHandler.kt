package com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration.handlers

import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.Post
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.Attachment
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.video.VideoAttachment
import com.pengrad.telegrambot.model.request.*
import com.pengrad.telegrambot.request.*

private const val maxDescriptionLength = 200 - 3
private const val descriptionCutter = "..."

class VideoHandler : PostHandler {
    override fun invoke(chatId: Long, post: Post, leftAttachments: MutableList<Attachment>): List<BaseRequest<*, *>> {
        return leftAttachments.filter {
            it is VideoAttachment
        }.let {
            if (it.isEmpty()) {
                emptyList()
            } else {
                it.mapNotNull {
                    (it as? VideoAttachment) ?.let {
                        videoAttachment ->
                        leftAttachments.remove(videoAttachment)
                        videoAttachment.video.player ?.let {
                            SendVideo(
                                chatId,
                                it
                            ).apply {
                                height(videoAttachment.video.height)
                                width(videoAttachment.video.width)
                                if (videoAttachment.video.duration == 0) {
                                    supportsStreaming(true)
                                } else {
                                    duration(videoAttachment.video.duration)
                                }
                            }
                        } ?: SendPhoto(
                            chatId,
                            videoAttachment.video.photo_800
                                ?: videoAttachment.video.photo_320
                                ?: videoAttachment.video.photo_130
                        ).replyMarkup(
                            InlineKeyboardMarkup(
                                arrayOf(
                                    InlineKeyboardButton(
                                        videoAttachment.video.title
                                    ).url(videoAttachment.video.url)
                                )
                            )
                        ).caption(
                            videoAttachment.video.description.let {
                                if (it.length > maxDescriptionLength) {
                                    it.substring(maxDescriptionLength) + descriptionCutter
                                } else {
                                    it
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
