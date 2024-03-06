package com.posite.clean.data.repository

import com.posite.clean.data.dto.user_list.UserDto
import com.posite.clean.data.service.TestService
import com.posite.clean.domain.model.UserList
import com.posite.clean.domain.repository.UserInfoRepository
import com.posite.clean.util.DataResult
import com.posite.clean.util.handleApi
import javax.inject.Inject

class UserInfoRepositoryImpl @Inject constructor(private val api: TestService) :
    UserInfoRepository {
    override suspend fun getAllUserInfo(page: Long): DataResult<UserList> {
        return handleApi({ api.getUsers(page) }) { it.toModel() }
    }

    override suspend fun getSingleUserInfo(id: Long): DataResult<UserDto> {
        return handleApi({ api.getSingleUser(id) }) { it.data }
    }
}