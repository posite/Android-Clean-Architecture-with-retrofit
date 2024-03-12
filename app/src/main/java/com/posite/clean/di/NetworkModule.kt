package com.posite.clean.di

import com.posite.clean.CleanApplication
import com.posite.clean.R
import com.posite.clean.data.service.naver.NaverService
import com.posite.clean.data.service.test.TestService
import com.posite.clean.util.DataStoreUtil
import com.posite.clean.util.HttpRequestInterceptor
import com.posite.clean.util.JwtInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MainRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NaverAuthRetrofit


    const val NETWORK_EXCEPTION_OFFLINE_CASE = "network status is offline"
    const val NETWORK_EXCEPTION_BODY_IS_NULL = "result body is null"

    @Provides
    @Singleton
    fun provideOKHttpClient(ds: DataStoreUtil): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpRequestInterceptor())
            .retryOnConnectionFailure(false)
            .addNetworkInterceptor(JwtInterceptor(ds))
            .build()
    }

    @Provides
    @Singleton
    @MainRetrofit
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(CleanApplication.getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @NaverAuthRetrofit
    fun provideNaverAuthRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(CleanApplication.getString(R.string.naver_base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideTestService(@MainRetrofit retrofit: Retrofit): TestService {
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun provideNaverService(@NaverAuthRetrofit retrofit: Retrofit): NaverService {
        return retrofit.buildService()
    }


    private inline fun <reified T> Retrofit.buildService(): T {
        return this.create(T::class.java)
    }
}