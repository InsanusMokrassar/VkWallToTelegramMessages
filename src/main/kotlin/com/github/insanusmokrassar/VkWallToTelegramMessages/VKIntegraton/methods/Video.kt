package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.methods

import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.Lists
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.Response
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.attachments.video.Video
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

private const val version = "5.78"

fun buildVideoIdentifier(
    id: Long,
    ownerId: Long,
    accessKey: String?
): String = "${ownerId}_$id".let {
    videos ->
    accessKey ?.let {
        "${videos}_$it"
    } ?: videos
}

interface Video {
    @GET("video.get?v=$version")
    fun get(
        @Query("owner_id")
        ownerId: Long,
        @Query("videos")
        videos: String
    ): Deferred<Response<Lists<Video>>>
}
