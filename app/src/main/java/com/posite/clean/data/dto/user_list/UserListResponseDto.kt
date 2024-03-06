package com.posite.clean.data.dto.user_list

import com.posite.clean.domain.model.UserList

data class UserListResponseDto(
    val `data`: List<UserDto>,
    val page: Int,
    val per_page: Int,
    val support: Support,
    val total: Int,
    val total_pages: Int
) {
    fun toModel() = UserList(this.data)
}