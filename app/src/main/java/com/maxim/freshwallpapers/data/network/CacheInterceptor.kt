package com.maxim.freshwallpapers.data.network

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class CacheInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val control = CacheControl.Builder()
            .maxAge(24, TimeUnit.HOURS)
            .build()
        return response.newBuilder()
            .header("Cache-Control", control.toString())
            .build()
    }
}