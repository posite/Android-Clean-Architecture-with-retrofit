package com.posite.clean.di

import com.posite.clean.data.repository.UserInfoRepositoryImpl
import com.posite.clean.data.service.TestService
import com.posite.clean.domain.repository.UserInfoRepository
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
    fun providesAuthRepository(api: TestService): UserInfoRepository =
        UserInfoRepositoryImpl(api)
}