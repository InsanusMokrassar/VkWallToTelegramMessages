package com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration

import com.github.insanusmokrassar.VkWallToTelegramMessages.Config
import com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration.handlers.handlersOrder
import com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration.handlers.postsLogger
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.NewPostCallback
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.*
import com.pengrad.telegrambot.response.MessagesResponse
import com.pengrad.telegrambot.response.SendResponse

class TelegramBotIntegration(
    config: Config
) {
    private val bot = TelegramBot.Builder(config.botApiToken).run {
        if (config.debug) {
            debug()
        }
        build()
    }

    private val chatId = config.chatId

    init {
        (if (config.debug) {
            bot.execute(
                SendMessage(
                    chatId,
                    "Bot was inited."
                ).parseMode(ParseMode.Markdown)
            )
        } else {
            bot.execute(
                GetChat(
                    chatId
                )
            )
        }).let {
            if (!it.isOk) {
                throw IllegalStateException("Can't send hello message to $chatId: $it")
            }
        }
    }

    val callback: NewPostCallback = {
        post ->
        val attachmentsMutable = post.flatAdaptedAttachments().toMutableList()
        val messagesIds = mutableListOf<Int>()
        try {
            handlersOrder.forEach {
                it(chatId, post, attachmentsMutable).forEach {
                    bot.execute(it).let {
                        if (it.isOk) {
                            when (it) {
                                is SendResponse -> messagesIds.add(it.message().messageId())
                                is MessagesResponse -> messagesIds.addAll(it.messages().map { it.messageId() })
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            postsLogger.error("${this::class.java.simpleName}: Error when try to post message, rollback.", e)
            messagesIds.forEach {
                bot.execute(
                    DeleteMessage(
                        chatId,
                        it
                    )
                )
            }
            throw e
        }
    }
}