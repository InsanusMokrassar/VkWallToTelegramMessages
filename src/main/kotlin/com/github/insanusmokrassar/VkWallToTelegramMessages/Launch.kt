package com.github.insanusmokrassar.VkWallToTelegramMessages

import com.github.insanusmokrassar.IObjectKRealisations.*
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.VKIntegration
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.VKMethodsHolder
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.ShowHelpException
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import java.util.concurrent.TimeUnit

private class LauncherArgumentsParser(parser: ArgParser) {
    val config_file by parser.positional("File path to config file")
}

fun main(args: Array<String>) {
    val config = try {
        ArgParser(args).parseInto(::LauncherArgumentsParser).run {
            load(config_file).toObject(Config::class.java)
        }
    } catch (e: ShowHelpException) {
        e.printAndExit()
    }
    val vkIntegration = VKIntegration(
        config,
        {
            it.forEach {
                println(it.toIObject())
            }
        },
        DB(config.adaptedDbPlace)
    )
    runBlocking {
        delay(10, TimeUnit.MINUTES)
    }
}
