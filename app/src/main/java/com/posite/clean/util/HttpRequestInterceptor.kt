package com.posite.clean.util

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class HttpRequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val originRequest = chain.request()
            Log.d("chain", "HttpRequestInterceptor: ${originRequest.url}")
            return chain.proceed(originRequest)
        } catch (e: Exception) {
            Log.d("", "HttpRequestInterceptor error: ${e.message}")
            throw e
        }
    }
}