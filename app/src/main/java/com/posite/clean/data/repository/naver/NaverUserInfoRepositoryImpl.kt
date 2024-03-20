package com.posite.clean.data.repository.naver

import com.posite.clean.data.datasource.naver.NaverUserInfoDataSource
import com.posite.clean.domain.model.naver.UserInfo
import com.posite.clean.domain.repository.naver.NaverUserInfoRepository
import com.posite.clean.util.DataResult
import com.posite.clean.util.NaverUserInfoMapperUtil
import javax.inject.Inject

class NaverUserInfoRepositoryImpl @Inject constructor(
    private val dataSource: NaverUserInfoDataSource,
    private val mapper: NaverUserInfoMapperUtil
) :
    NaverUserInfoRepository {
    override suspend fun getNaverUserInfo(): DataResult<UserInfo> {
        return mapper(dataSource.fetchNaverUserInfo())
    }
}