package com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration

import com.github.insanusmokrassar.VkWallToTelegramMessages.Config
import com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration.handlers.handlersOrder
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.NewPostCallback
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.SendMessage

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
        bot.execute(
            SendMessage(
                chatId,
                "Bot was inited."
            ).parseMode(ParseMode.Markdown)
        ).let {
            if (!it.isOk) {
                throw IllegalStateException("Can't send hello message to $chatId: ${it.description()}")
            }
        }
    }

    val callback: NewPostCallback = {
        post ->
        val attachmentsMutable = post.flatAdaptedAttachments().toMutableList()
        handlersOrder.forEach {
            it(chatId, post, attachmentsMutable).forEach {
                bot.execute(it)
            }
        }
    }
}