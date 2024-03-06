package com.posite.clean.data.service

import com.posite.clean.data.dto.user_info.UserInfoResponseDto
import com.posite.clean.data.dto.user_list.UserListResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TestService {
    @GET("/api/users")
    suspend fun getUsers(@Query("page") page: Long): Response<UserListResponseDto>

    @GET("/api/users/{id}")
    suspend fun getSingleUser(@Path("id") id: Long): Response<UserInfoResponseDto>

}