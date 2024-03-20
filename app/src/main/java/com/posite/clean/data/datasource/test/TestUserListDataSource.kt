package com.posite.clean.data.datasource.test

import com.posite.clean.data.dto.user_list.UserListResponseDto
import com.posite.clean.data.service.test.TestService
import com.posite.clean.util.DataResult
import com.posite.clean.util.handleApi
import javax.inject.Inject

class TestUserListDataSource @Inject constructor(private val service: TestService) {
    suspend fun fetchUsers(page: Long): DataResult<UserListResponseDto> {
        return handleApi({ service.getUsers(page) }) { it }
    }
}