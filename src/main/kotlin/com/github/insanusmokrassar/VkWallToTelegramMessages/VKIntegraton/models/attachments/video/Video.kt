package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.video

import com.github.insanusmokrassar.IObjectKRealisations.toIObject
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.VKMethodsHolder
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.methods.buildVideoIdentifier
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.AttachmentBase
import com.google.gson.annotations.SerializedName
import java.util.concurrent.TimeUnit

private val resolversMap = mapOf(
    "YouTube" to {
        video: Video ->
        ""
    }
)

class Video(
    val title: String,
    val duration: Int,
    val description: String,
    val comments: Long,
    val views: Long,
    val width: Int,
    val height: Int,
    val platform: String?,
    val photo_130: String?,
    val photo_320: String?,
    val photo_800: String?,
    val first_frame_130: String?,
    val first_frame_160: String?,
    val first_frame_320: String?,
    val first_frame_800: String?,
    val player: String?
) : AttachmentBase() {
    @SerializedName("adding_date")
    override val date: Long = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())

    val url: String
        get() = "https://vk.com/video$ownerId-$id"
}
