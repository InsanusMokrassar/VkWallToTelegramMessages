package com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration

import com.github.insanusmokrassar.IObjectK.extensions.toJsonString
import com.github.insanusmokrassar.IObjectKRealisations.toIObject
import com.github.insanusmokrassar.IObjectKRealisations.toJson
import com.github.insanusmokrassar.VkWallToTelegramMessages.Config
import com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration.handlers.handlersOrder
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.NewPostsCallback
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.Post
import com.pengrad.telegrambot.Callback
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

    val callback: NewPostsCallback = {
        it.forEach {
            post ->
            val attachmentsMutable = post.adaptedAttachments.toMutableList()
            handlersOrder.forEach {
                it(chatId, post, attachmentsMutable).forEach {
                    bot.execute(it)
                }
            }
        }
    }
}