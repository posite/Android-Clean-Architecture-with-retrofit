package com.posite.clean.di

import com.posite.clean.data.repository.naver.NaverUserInfoRepositoryImpl
import com.posite.clean.data.repository.test.UserInfoRepositoryImpl
import com.posite.clean.data.service.naver.NaverService
import com.posite.clean.data.service.test.TestService
import com.posite.clean.domain.repository.naver.NaverUserInfoRepository
import com.posite.clean.domain.repository.test.UserInfoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideUserInfoRepository(api: TestService): UserInfoRepository =
        UserInfoRepositoryImpl(api)

    @Provides
    @ViewModelScoped
    fun provideNaverUserInfoRepository(api: NaverService): NaverUserInfoRepository =
        NaverUserInfoRepositoryImpl(api)
}