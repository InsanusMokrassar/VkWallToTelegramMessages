package com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration.handlers

import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.Post
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.Attachment
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.TextAttachment
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.link.LinkAttachment
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage

class LinkHandler : PostHandler {
    override fun invoke(chatId: Long, post: Post, leftAttachments: MutableList<Attachment>): List<BaseRequest<*, *>> {
        return leftAttachments.filter {
            it is LinkAttachment
        }.map {
            linkAttachment ->
            leftAttachments.remove(linkAttachment)
            linkAttachment as LinkAttachment
            val link = linkAttachment.link
            val text = link.caption ?:let {
                if (link.title.isNotEmpty()) {
                    link.title
                } else {
                    if (link.description.isNotEmpty()) {
                        link.description
                    } else {
                        leftAttachments.firstOrNull { it is TextAttachment } ?.let {
                            leftAttachments.remove(it)
                            it as TextAttachment
                            it.text
                        } ?:let {
                            link.url
                        }
                    }
                }
            }
            SendMessage(
                chatId,
                "[$text](${link.url})"
            ).parseMode(
                ParseMode.Markdown
            )
        }
    }
}
