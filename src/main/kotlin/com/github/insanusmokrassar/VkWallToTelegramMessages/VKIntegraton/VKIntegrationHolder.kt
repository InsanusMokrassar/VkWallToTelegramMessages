package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton

import com.github.insanusmokrassar.IObjectKRealisations.doUsingDefaultGSON
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

private const val vkRoot = "https://api.vk.com/method/"

class VKIntegrationHolder(
    accessToken: String
) {
    val wall: Wall

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
    }
}
