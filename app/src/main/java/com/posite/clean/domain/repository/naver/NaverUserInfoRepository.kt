package com.posite.clean.domain.repository.naver

import com.posite.clean.domain.model.naver.UserInfo
import com.posite.clean.util.DataResult

interface NaverUserInfoRepository {

    suspend fun getNaverUserInfo(): DataResult<UserInfo>
}