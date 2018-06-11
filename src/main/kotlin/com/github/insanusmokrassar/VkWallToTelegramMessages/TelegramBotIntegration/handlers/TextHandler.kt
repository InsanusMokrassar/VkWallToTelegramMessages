package com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration.handlers

import com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration.breakByLength
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.Post
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.Attachment
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.TextAttachment
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage

class TextHandler : PostHandler {
    override fun invoke(chatId: Long, post: Post, leftAttachments: MutableList<Attachment>): List<BaseRequest<*, *>> {
        return leftAttachments.filter {
            it is TextAttachment
        }.flatMap {
            leftAttachments.remove(it)
            (it as TextAttachment).text.breakByLength().map {
                SendMessage(
                    chatId,
                    it
                )
            }
        }
    }
}
