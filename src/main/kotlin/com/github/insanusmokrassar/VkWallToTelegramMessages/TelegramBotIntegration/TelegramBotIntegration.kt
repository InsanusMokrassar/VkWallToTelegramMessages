package com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration

import com.github.insanusmokrassar.IObjectKRealisations.toIObject
import com.github.insanusmokrassar.VkWallToTelegramMessages.Config
import com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration.handlers.handlersOrder
import com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration.handlers.postsLogger
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.NewPostCallback
import com.pengrad.telegrambot.Callback
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.*
import com.pengrad.telegrambot.response.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit

private const val defaultTimeout = /*2000*/2L
private val defaultTimeoutTimeUnit = TimeUnit.MINUTES

private class SendCallback<T : BaseRequest<T, R>, R : BaseResponse> : Callback<T, R> {
    override fun onFailure(request: T, e: IOException?) {
        postsLogger.error("${this::class.java.simpleName}: Can't perform request: ${request.toIObject()}", e)
    }

    override fun onResponse(request: T, response: R) {
        postsLogger.info("${this::class.java.simpleName}: performed request: ${request.toIObject()}. Response: ${response.toIObject()}")
    }

}

class TelegramBotIntegration(
    config: Config
) {
    private val bot = TelegramBot.Builder(
        config.botApiToken
    ).run {
        okHttpClient(
            OkHttpClient.Builder().run {
                connectTimeout(defaultTimeout, defaultTimeoutTimeUnit)
                readTimeout(defaultTimeout, defaultTimeoutTimeUnit)
                writeTimeout(defaultTimeout, defaultTimeoutTimeUnit)
                if (config.debug) {
                    addInterceptor(
                        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                    )
                }
                build()
            }
        )
        build()
    }

    private val chatId = config.chatId

    private val deleteCallback = SendCallback<DeleteMessage, BaseResponse>()

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
        handlersOrder.flatMap {
            it(chatId, post, attachmentsMutable)
        }.forEach {
            try {
                bot.execute(it).let {
                    if (it.isOk) {
                        when (it) {
                            is SendResponse -> messagesIds.add(it.message().messageId())
                            is MessagesResponse -> messagesIds.addAll(it.messages().map { it.messageId() })
                        }
                    }
                }
            } catch (e: Exception) {
                postsLogger.error("${this::class.java.simpleName}: Error when try to handle post. Rollback", e)
                messagesIds.forEach {
                    bot.execute(
                        DeleteMessage(
                            chatId,
                            it
                        ),
                        deleteCallback
                    )
                }
                throw e
            }
        }
    }
}