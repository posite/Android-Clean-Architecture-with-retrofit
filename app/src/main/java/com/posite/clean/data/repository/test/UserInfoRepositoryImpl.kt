package com.posite.clean.data.repository.test

import com.posite.clean.data.datasource.test.TestUserInfoDataSource
import com.posite.clean.data.datasource.test.TestUserListDataSource
import com.posite.clean.data.dto.user_list.UserDto
import com.posite.clean.domain.model.test.UserList
import com.posite.clean.domain.repository.test.UserInfoRepository
import com.posite.clean.util.DataResult
import com.posite.clean.util.TestUserInfoMapperUtil
import com.posite.clean.util.TestUserListMapperUtil
import javax.inject.Inject

class UserInfoRepositoryImpl @Inject constructor(
    private val userListDataSource: TestUserListDataSource,
    private val userInfoDataSource: TestUserInfoDataSource,
    private val userListMapper: TestUserListMapperUtil,
    private val userInfoMapper: TestUserInfoMapperUtil
) :
    UserInfoRepository {
    override suspend fun getAllUserInfo(page: Long): DataResult<UserList> {
        return userListMapper(userListDataSource.fetchUsers(page))
    }

    override suspend fun getSingleUserInfo(id: Long): DataResult<UserDto> {
        return userInfoMapper(userInfoDataSource.fetchUserInfo(id))
    }
}