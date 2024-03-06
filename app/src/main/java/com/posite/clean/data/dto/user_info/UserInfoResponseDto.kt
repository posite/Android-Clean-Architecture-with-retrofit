package com.posite.clean.data.dto.user_info

import com.posite.clean.data.dto.user_list.UserDto

data class UserInfoResponseDto(
    val `data`: UserDto,
    val support: Support
)