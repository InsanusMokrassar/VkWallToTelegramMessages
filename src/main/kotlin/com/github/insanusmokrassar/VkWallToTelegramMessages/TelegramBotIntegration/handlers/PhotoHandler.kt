package com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration.handlers

import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.Post
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.Attachment
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.photo.PhotoAttachment
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendPhoto

class PhotoHandler : PostHandler {
    override fun invoke(chatId: Long, post: Post, leftAttachments: MutableList<Attachment>): List<BaseRequest<*, *>> {
        return leftAttachments.filter {
            it is PhotoAttachment
        }.mapNotNull {
            (it as PhotoAttachment).let {
                leftAttachments.remove(it)
                SendPhoto(
                    chatId,
                    it.photo.biggestSize ?. url ?: throw IllegalArgumentException("Photo must have at least one size")
                ).caption(
                    it.photo.text
                )
            }
        }
    }
}
