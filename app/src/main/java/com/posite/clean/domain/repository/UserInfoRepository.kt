package com.posite.clean.domain.repository

import com.posite.clean.data.dto.user_list.UserDto
import com.posite.clean.domain.model.UserList
import com.posite.clean.util.DataResult

interface UserInfoRepository {
    suspend fun getAllUserInfo(page: Long): DataResult<UserList>
    
    suspend fun getSingleUserInfo(id: Long): DataResult<UserDto>
}