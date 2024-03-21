package com.posite.clean.data.repository.test

import com.posite.clean.data.datasource.test.TestDataSource
import com.posite.clean.data.dto.user_list.UserDto
import com.posite.clean.domain.model.test.UserList
import com.posite.clean.domain.repository.test.UserInfoRepository
import com.posite.clean.util.DataResult
import com.posite.clean.util.TestUserInfoMapperUtil
import com.posite.clean.util.TestUserListMapperUtil
import javax.inject.Inject

class UserInfoRepositoryImpl @Inject constructor(
    private val testDataSource: TestDataSource,
    private val userListMapper: TestUserListMapperUtil,
    private val userInfoMapper: TestUserInfoMapperUtil
) :
    UserInfoRepository {
    override suspend fun getAllUserInfo(page: Long): DataResult<UserList> {
        return userListMapper(testDataSource.fetchUsers(page))
    }

    override suspend fun getSingleUserInfo(id: Long): DataResult<UserDto> {
        return userInfoMapper(testDataSource.fetchUserInfo(id))
    }
}