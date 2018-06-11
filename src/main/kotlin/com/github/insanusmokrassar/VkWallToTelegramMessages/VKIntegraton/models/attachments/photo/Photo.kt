package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.photo

import com.google.gson.annotations.SerializedName
import java.util.concurrent.TimeUnit

class Photo(
    val id: Long,
    @SerializedName("owner_id")
    val ownerId: Long,
    @SerializedName("user_id")
    val userId: Long,
    val sizes: List<Size>,
    @SerializedName("album_id")
    val albumId: Long? = null,
    val text: String? = null,
    val date: Long = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
) {
    val uploadedByCommunity: Boolean
        get() = userId == 100L
}
