package com.posite.clean.util

import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class JwtInterceptor @Inject constructor(@ApplicationContext private val ds: DataStoreUtil) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        Log.d("accessToken", "토큰 뽑기 전")
        val token: String = runBlocking { ds.loadAccessToken() }
        Log.d("accessToken", token)

        token.let {
            builder.addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(builder.build())
    }

}