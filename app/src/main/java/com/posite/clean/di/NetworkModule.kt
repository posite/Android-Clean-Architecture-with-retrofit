package com.posite.clean.di

import com.posite.clean.CleanApplication
import com.posite.clean.R
import com.posite.clean.util.HttpRequestInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    const val NETWORK_EXCEPTION_OFFLINE_CASE = "network status is offline"
    const val NETWORK_EXCEPTION_BODY_IS_NULL = "result body is null"

    @Provides
    @Singleton
    fun provideOKHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpRequestInterceptor())
            .retryOnConnectionFailure(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(CleanApplication.getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /*
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.buildService()
    }
    */


    private inline fun <reified T> Retrofit.buildService(): T {
        return this.create(T::class.java)
    }
}