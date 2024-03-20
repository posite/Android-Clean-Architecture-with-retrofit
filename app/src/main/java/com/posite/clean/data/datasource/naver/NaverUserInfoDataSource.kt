package com.posite.clean.data.datasource.naver

import com.posite.clean.data.dto.naver.UserInfoResponseDto
import com.posite.clean.data.service.naver.NaverService
import com.posite.clean.util.DataResult
import com.posite.clean.util.handleApi
import javax.inject.Inject

class NaverUserInfoDataSource @Inject constructor(private val service: NaverService) {
    suspend fun fetchNaverUserInfo(): DataResult<UserInfoResponseDto> {
        return handleApi({ service.getUserInfo() }) { it }
    }
}