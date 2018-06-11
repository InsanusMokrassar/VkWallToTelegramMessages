package com.github.insanusmokrassar.VkWallToTelegramMessages

import com.github.insanusmokrassar.IObjectKRealisations.doUsingDefaultGSON
import org.joda.time.DateTime
import java.io.File

class Settings(
    val lastReadDate: Long = DateTime.now().minusDays(1).millis
) {
    constructor(
        settings: Settings
    ) : this(
        settings.lastReadDate
    )

    fun verify(settings: Settings): Boolean {
        return settings.lastReadDate >= lastReadDate
    }
}

class DB(
    filePath: String
) {
    private val file = File(filePath)

    var settings: Settings = doUsingDefaultGSON {
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
            file.writeText("{}")
        }
        it.fromJson(file.readText(), Settings::class.java)
    }
        get() = Settings(field)
        @Synchronized
        @Throws(IllegalArgumentException::class)
        set(value) {
            if (field.verify(value)) {
                field = value
                doUsingDefaultGSON {
                    it.toJson(value)
                }.also {
                    file.delete()
                    file.createNewFile()
                    file.writeText(it)
                }
            }
        }
}
