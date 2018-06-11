package com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration.handlers

import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.Post
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.Attachment
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.TextAttachment
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.photo.PhotoAttachment
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendPhoto

class PhotoHandler : PostHandler {
    override fun invoke(chatId: Long, post: Post, leftAttachments: MutableList<Attachment>): List<BaseRequest<*, *>> {
        return leftAttachments.filter {
            it is PhotoAttachment
        }.mapNotNull {
            (it as PhotoAttachment).let {
                photoAttachment ->
                leftAttachments.remove(photoAttachment)
                SendPhoto(
                    chatId,
                    photoAttachment.photo.biggestSize ?. url ?: throw IllegalArgumentException("Photo must have at least one size")
                ).caption(
                    photoAttachment.photo.text
                ).also {
                    if (leftAttachments.size == 1 && leftAttachments.first() is TextAttachment && !photoAttachment.photo.textIsCorrect) {
                        it.caption(
                            (leftAttachments.removeAt(0) as TextAttachment).text
                        )
                    }
                }
            }
        }
    }
}
