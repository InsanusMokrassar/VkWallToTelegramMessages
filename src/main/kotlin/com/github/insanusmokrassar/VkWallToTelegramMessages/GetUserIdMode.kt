package com.github.insanusmokrassar.VkWallToTelegramMessages

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener

fun getUserIdMode(
    botToken: String
) {
    val bot = TelegramBot.Builder(botToken).build()
    bot.setUpdatesListener {
        it.forEach {
            it.message() ?.let {
                println("Private or group message: ${it.chat().id()}\nText: ${it.text()}")
            } ?: it.channelPost() ?.let {
                println("Channel message: ${it.chat().id()}\nText: ${it.text()}")
            }
        }
        UpdatesListener.CONFIRMED_UPDATES_ALL
    }
}
