package com.posite.clean.di

import com.posite.clean.data.datasource.naver.NaverUserInfoDataSource
import com.posite.clean.data.datasource.test.TestUserInfoDataSource
import com.posite.clean.data.datasource.test.TestUserListDataSource
import com.posite.clean.data.repository.naver.NaverUserInfoRepositoryImpl
import com.posite.clean.data.repository.test.UserInfoRepositoryImpl
import com.posite.clean.domain.repository.naver.NaverUserInfoRepository
import com.posite.clean.domain.repository.test.UserInfoRepository
import com.posite.clean.util.NaverUserInfoMapperUtil
import com.posite.clean.util.TestUserInfoMapperUtil
import com.posite.clean.util.TestUserListMapperUtil
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
    fun provideUserInfoRepository(
        userInfoDataSource: TestUserInfoDataSource,
        userListDataSource: TestUserListDataSource
    ): UserInfoRepository =
        UserInfoRepositoryImpl(
            userListDataSource,
            userInfoDataSource,
            TestUserListMapperUtil(),
            TestUserInfoMapperUtil()
        )

    @Provides
    @ViewModelScoped
    fun provideNaverUserInfoRepository(dataSource: NaverUserInfoDataSource): NaverUserInfoRepository =
        NaverUserInfoRepositoryImpl(dataSource, NaverUserInfoMapperUtil())
}