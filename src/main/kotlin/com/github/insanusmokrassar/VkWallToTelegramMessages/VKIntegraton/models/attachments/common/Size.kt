package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.common

import com.google.gson.annotations.SerializedName

class Size (
    val height: Int,
    val width: Int,
    val type: String,
    @SerializedName("url", alternate = ["src"])
    val url: String
) : Comparable<Size> {
    private val size: Int
        get() = height * width

    override fun compareTo(other: Size): Int {
        return size.compareTo(other.size)
    }
}
