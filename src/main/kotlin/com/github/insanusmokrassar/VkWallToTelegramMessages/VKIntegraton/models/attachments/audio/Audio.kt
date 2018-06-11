package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.audio

import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.AttachmentBase
import com.google.gson.annotations.SerializedName

class Audio(
    val duration: Int,
    val artist: String? = null,
    val title: String? = null,
    val url: String? = null,
    @SerializedName("lyrics_id")
    val lyricsId: Int? = null,
    @SerializedName("album_id")
    val albumId: Int? = null,
    @SerializedName("genre_id")
    val genreId: Int? = null
) : AttachmentBase() {
    val audioUrlCorrect: Boolean
        get() = url ?.isNotEmpty() ?: false
}
