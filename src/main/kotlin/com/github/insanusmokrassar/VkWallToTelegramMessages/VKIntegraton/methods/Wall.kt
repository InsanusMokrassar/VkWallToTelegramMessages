package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.methods

import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.PostsResponse
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

private const val version = "5.78"

interface Wall {

    @GET("wall.get?v=$version")
    fun get(
        @Query("domain") domain: String,
        @Query("offset") offset: Int = 0,
        @Query("count") count: Int = 20
    ) : Deferred<PostsResponse>

    @GET("wall.get?v=$version")
    fun get(
        @Query("owner_id") ownerId: Long,
        @Query("offset") offset: Int = 0,
        @Query("count") count: Int = 20
    ) : Deferred<PostsResponse>
}
