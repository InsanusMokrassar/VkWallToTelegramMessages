package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton

import com.github.insanusmokrassar.IObjectK.interfaces.IObject
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.models.*
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

private const val version = "5.78"

interface Wall {

    @GET("wall.get?v=$version")
    fun get(
        @Query("domain") domain: String,
        @Query("offset") offset: Long = 0,
        @Query("count") count: Long = 20
    ) : Deferred<PostsResponse>
}
