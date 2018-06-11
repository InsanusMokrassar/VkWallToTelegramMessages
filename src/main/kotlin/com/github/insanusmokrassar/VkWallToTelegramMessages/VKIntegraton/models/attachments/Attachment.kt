package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments

import com.google.gson.annotations.SerializedName
import java.util.concurrent.TimeUnit

open class Attachment(
    val type: String
) {
    val id: Long = -1
    @SerializedName("owner_id")
    val ownerId: Long = -1
    @SerializedName("access_key")
    val accessKey: String = ""
    val date: Long = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
}
