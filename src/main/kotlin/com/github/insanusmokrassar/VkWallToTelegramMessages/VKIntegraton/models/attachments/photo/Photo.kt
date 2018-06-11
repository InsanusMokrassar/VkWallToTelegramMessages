package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.photo

import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.AttachmentBase
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.common.Size
import com.google.gson.annotations.SerializedName

class Photo(
    @SerializedName("user_id")
    val userId: Long,
    val sizes: List<Size>,
    @SerializedName("album_id")
    val albumId: Long? = null,
    val text: String? = null
) : AttachmentBase() {
    val uploadedByCommunity: Boolean
        get() = userId == 100L

    val biggestSize: Size?
        get() = sizes.max()
}
