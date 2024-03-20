package com.posite.clean.util

import com.posite.clean.data.dto.user_info.UserInfoResponseDto
import com.posite.clean.data.dto.user_list.UserDto

class TestUserInfoMapperUtil {

    operator fun invoke(data: DataResult<UserInfoResponseDto>) = changeInstanceType(data)

    fun changeInstanceType(instance: DataResult<UserInfoResponseDto>): DataResult<UserDto> {
        return when (instance) {
            is DataResult.Success -> {
                DataResult.Success(
                    instance.data.data
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