package com.posite.clean.presentation.ui.single

import com.posite.clean.data.dto.user_list.UserDto
import kotlinx.coroutines.flow.StateFlow


interface UserInfoViewModel {
    val userData: StateFlow<UserDto>

    fun getSingeUserInfo(id: Long)
}