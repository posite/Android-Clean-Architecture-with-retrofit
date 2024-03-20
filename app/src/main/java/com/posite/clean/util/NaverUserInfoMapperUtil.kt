package com.posite.clean.util

import com.posite.clean.data.dto.naver.UserInfoResponseDto
import com.posite.clean.domain.model.naver.UserInfo

class NaverUserInfoMapperUtil {

    operator fun invoke(data: DataResult<UserInfoResponseDto>) = changeInstanceType(data)

    fun changeInstanceType(instance: DataResult<UserInfoResponseDto>): DataResult<UserInfo> {
        return when (instance) {
            is DataResult.Success -> {
                DataResult.Success(
                    UserInfo(
                        instance.data.response.nickname,
                        instance.data.response.profile_image
                    )
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