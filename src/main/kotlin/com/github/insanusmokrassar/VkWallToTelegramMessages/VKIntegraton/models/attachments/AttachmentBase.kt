package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments

import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.VKMethodsHolder
import com.google.gson.annotations.SerializedName
import java.util.concurrent.TimeUnit

open class AttachmentBase {
    open val id: Long = -1
    @SerializedName("owner_id")
    open val ownerId: Long = -1
    @SerializedName("access_key")
    open val accessKey: String = ""
    open val date: Long = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())

    open suspend fun prepareAttachment(vkMethodsHolder: VKMethodsHolder): AttachmentBase {
        return this
    }
}