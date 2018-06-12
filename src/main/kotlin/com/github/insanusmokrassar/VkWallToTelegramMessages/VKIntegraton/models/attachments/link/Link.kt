package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.link

import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.AttachmentBase
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.photo.Photo
import com.google.gson.annotations.SerializedName

class Link(
    val url: String,
    val title: String,
    val description: String,
    val caption: String? = null,
    val photo: Photo? = null,
    @SerializedName("preview_page")
    val previewPage: String? = null,
    @SerializedName("preview_url")
    val previewUrl: String? = null
) : AttachmentBase()
