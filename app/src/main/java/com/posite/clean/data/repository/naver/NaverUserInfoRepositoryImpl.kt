package com.posite.clean.data.repository.naver

import com.posite.clean.data.service.naver.NaverService
import com.posite.clean.domain.model.naver.UserInfo
import com.posite.clean.domain.repository.naver.NaverUserInfoRepository
import com.posite.clean.util.DataResult
import com.posite.clean.util.handleApi
import javax.inject.Inject

class NaverUserInfoRepositoryImpl @Inject constructor(private val api: NaverService) :
    NaverUserInfoRepository {
    override suspend fun getNaverUserInfo(): DataResult<UserInfo> {
        return handleApi({ api.getUserInfo() }) { response ->
            UserInfo(
                response.response.nickname,
                response.response.profile_image
            )
        }
    }
}