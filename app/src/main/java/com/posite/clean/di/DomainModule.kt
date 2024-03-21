package com.posite.clean.di

import com.posite.clean.data.datasource.naver.NaverUserInfoDataSource
import com.posite.clean.data.datasource.test.TestDataSource
import com.posite.clean.data.service.naver.NaverService
import com.posite.clean.data.service.test.TestService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    @Singleton
    fun provideNaverUserInfoDataSource(service: NaverService): NaverUserInfoDataSource =
        NaverUserInfoDataSource(service)

    @Provides
    @Singleton
    fun provideTestDataSource(service: TestService): TestDataSource =
        TestDataSource(service)
}