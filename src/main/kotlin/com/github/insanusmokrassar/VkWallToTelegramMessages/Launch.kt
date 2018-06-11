package com.github.insanusmokrassar.VkWallToTelegramMessages

import com.github.insanusmokrassar.IObjectKRealisations.*
import com.github.insanusmokrassar.VkWallToTelegramMessages.TelegramBotIntegration.TelegramBotIntegration
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.VKIntegration
import com.xenomachina.argparser.*
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import java.util.concurrent.TimeUnit

private class LauncherArgumentsParser(parser: ArgParser) {
    val getUserIdMode by parser.flagging("Enable get user ID mode to know your id for bot. " +
        "This mode will usefull if you want to receive messages in private messages/group/channel or in channel " +
        "which have no username.\n\n**** WARNING! ****\n\nPrivate messages with bot require your id, not username!"
    ).default(false)
    val config_file by parser.positional("File path to config file")
}

fun main(args: Array<String>) {
    try {
        ArgParser(args).parseInto(::LauncherArgumentsParser).run {
            val config = load(config_file).toObject(Config::class.java)
            if (getUserIdMode) {
                getUserIdMode(config.botApiToken)
            } else {
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
        }
    } catch (e: ShowHelpException) {
        e.printAndExit()
    }
}
