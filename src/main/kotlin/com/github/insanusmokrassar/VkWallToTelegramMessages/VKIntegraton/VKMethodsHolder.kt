package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton

import com.github.insanusmokrassar.IObjectKRealisations.doUsingDefaultGSON
import com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton.methods.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val vkRoot = "https://api.vk.com/method/"

class VKMethodsHolder(
    accessToken: String
) {
    val wall: Wall
    val video: Video

    init {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(VKInterceptor(accessToken))
            .build()

        val retrofit = Retrofit.Builder().run {
            doUsingDefaultGSON {
                addConverterFactory(
                    GsonConverterFactory.create(it)
                )
            }
            addCallAdapterFactory(CoroutineCallAdapterFactory())
            client(okHttpClient)
            baseUrl(vkRoot)
            build()
        }

        wall = retrofit.create(Wall::class.java)
        video = retrofit.create(Video::class.java)
    }
}
