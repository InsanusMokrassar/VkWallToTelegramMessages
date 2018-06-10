package com.github.insanusmokrassar.VkWallToTelegramMessages.VKIntegraton

import okhttp3.Interceptor
import okhttp3.Response

private const val accessTokenField = "access_token"

class VKInterceptor(
    private val accessToken: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response {
        chain ?. request() ?. let {
            baseRequest ->
            baseRequest.newBuilder().run {
                url(
                    baseRequest.url().newBuilder().run {
                        addQueryParameter(accessTokenField, accessToken)
                        build()
                    }
                )
                build()
            }
        } ?. let {
            return chain.proceed(it)
        } ?: throw IllegalArgumentException("Interceptor require request in chain")
    }
}
