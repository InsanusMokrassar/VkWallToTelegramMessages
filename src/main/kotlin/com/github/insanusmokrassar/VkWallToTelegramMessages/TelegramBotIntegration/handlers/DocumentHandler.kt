package com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration.handlers

import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.Post
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.Attachment
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.doc.DocumentAttachment
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendDocument
import java.net.URL


class DocumentHandler : PostHandler {
    override fun invoke(
        chatId: Long,
        post: Post,
        leftAttachments: MutableList<Attachment>
    ): List<BaseRequest<*, *>> {
        return leftAttachments.filter {
            it is DocumentAttachment
        }.let {
            it.mapNotNull {
                (it as? DocumentAttachment) ?.let {
                    leftAttachments.remove(it)
                    SendDocument(
                        chatId,
                        URL(it.doc.simpleUrl).openStream().readBytes()
                    ).fileName(
                        it.doc.title
                    )
                }
            }
        }
    }
}
