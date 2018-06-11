package com.github.insanusmokrassar.VkWallToTelegramMessages

import java.io.File

private val separator = File.separator

const val WALL_PATH = "WALL"
const val CHAT_ID_PATH = "CHAT_ID"

class Config(
    val accessToken: String,
    val botApiToken: String,
    val wallDomain: String,
    val chatId: Long,
    val dbPlace : String? = null,
    val debug: Boolean = false
) {
    val adaptedDbPlace: String
        get() = (
            dbPlace ?: "${System.getenv("HOME")}$separator.config${separator}VkWallToTelegramMessages$separator$WALL_PATH$separator$CHAT_ID_PATH"
            )
            .replace(WALL_PATH, wallDomain)
            .replace(CHAT_ID_PATH, chatId.toString())
}