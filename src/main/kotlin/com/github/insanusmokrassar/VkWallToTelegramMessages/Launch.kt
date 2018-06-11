package com.github.insanusmokrassar.VkWallToTelegramMessages

import com.github.insanusmokrassar.IObjectKRealisations.*
import com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration.TelegramBotIntegration
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.VKIntegration
import com.xenomachina.argparser.*
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import java.util.concurrent.TimeUnit

private class LauncherArgumentsParser(parser: ArgParser) {
    val config_file by parser.positional("File path to config file")
}

fun main(args: Array<String>) {
    try {
        ArgParser(args).parseInto(::LauncherArgumentsParser).run {
            val config = load(config_file).toObject(Config::class.java)
            val telegramBotIntegration = TelegramBotIntegration(
                config
            )
            val vkIntegration = VKIntegration(
                config,
                telegramBotIntegration.callback,
                DB(config.adaptedDbPlace)
            )
            runBlocking {
                delay(10, TimeUnit.MINUTES)
            }
        }
    } catch (e: ShowHelpException) {
        e.printAndExit()
    }
}
