package com.posite.clean.util

import com.posite.clean.data.dto.user_list.UserListResponseDto
import com.posite.clean.domain.model.test.UserList

class TestUserListMapperUtil {

    operator fun invoke(data: DataResult<UserListResponseDto>) = changeInstanceType(data)

    fun changeInstanceType(instance: DataResult<UserListResponseDto>): DataResult<UserList> {
        return when (instance) {
            is DataResult.Success -> {
                DataResult.Success(
                    UserList(instance.data.data)
                )
            }

            is DataResult.Fail -> {
                DataResult.Fail(instance.statusCode, instance.message)
            }

            is DataResult.Error -> {
                DataResult.Error(instance.exception)
            }
        }
    }
}