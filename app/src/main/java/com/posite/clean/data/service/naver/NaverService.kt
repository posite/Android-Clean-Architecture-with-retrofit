package com.posite.clean.data.service.naver

import com.posite.clean.data.dto.naver.UserInfoResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface NaverService {
    @GET("/v1/nid/me")
    suspend fun getUserInfo(): Response<UserInfoResponseDto>
}