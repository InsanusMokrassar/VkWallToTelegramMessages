package com.github.insanusmokrassar.VkWallToTelegramMessages

import com.github.insanusmokrassar.IObjectKRealisations.*
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.VKIntegrationHolder
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.ShowHelpException
import kotlinx.coroutines.experimental.runBlocking

private class LauncherArgumentsParser(parser: ArgParser) {
    val config_file by parser.positional("File path to config file")
}

class Config(
    val accessToken: String,
    val botApiToken: String,
    val wallDomain: String,
    val chatId: String,
    val debug: Boolean = false
)

fun main(args: Array<String>) {
    val config = try {
        ArgParser(args).parseInto(::LauncherArgumentsParser).run {
            load(config_file).toObject(Config::class.java)
        }
    } catch (e: ShowHelpException) {
        e.printAndExit()
    }
    VKIntegrationHolder(config.accessToken).apply {
        runBlocking {
            wall.get(
                config.wallDomain
            ).await().let {
                println(it)
                it.response.items.forEach {
                    println(it.toIObject())
                    println("Attachments:")
                    it.adaptedAttachments.forEach {
                        println(it.toIObject())
                    }
                }
            }
        }
    }
}
