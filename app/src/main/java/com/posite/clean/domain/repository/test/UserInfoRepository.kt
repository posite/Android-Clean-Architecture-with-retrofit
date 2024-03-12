package com.posite.clean.domain.repository.test

import com.posite.clean.data.dto.user_list.UserDto
import com.posite.clean.domain.model.test.UserList
import com.posite.clean.util.DataResult

interface UserInfoRepository {
    suspend fun getAllUserInfo(page: Long): DataResult<UserList>

    suspend fun getSingleUserInfo(id: Long): DataResult<UserDto>
}