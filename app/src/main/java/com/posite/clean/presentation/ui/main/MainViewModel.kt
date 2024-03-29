package com.posite.clean.presentation.ui.main

import com.posite.clean.domain.model.test.UserList
import kotlinx.coroutines.flow.StateFlow


interface MainViewModel {
    val userList: StateFlow<UserList>
    fun getUserList(page: Long)
}