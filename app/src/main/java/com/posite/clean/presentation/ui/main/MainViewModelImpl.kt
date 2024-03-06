package com.posite.clean.presentation.ui.main

import androidx.lifecycle.viewModelScope
import com.posite.clean.domain.model.UserList
import com.posite.clean.domain.usecase.GetAllUserInfoUseCase
import com.posite.clean.presentation.base.BaseViewModel
import com.posite.clean.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModelImpl @Inject constructor(
    private val getAllUserInfoUseCase: GetAllUserInfoUseCase
) : BaseViewModel(), MainViewModel {
    private val _userList: MutableStateFlow<UserList> = MutableStateFlow(UserList(emptyList()))
    override val userList: StateFlow<UserList>
        get() = _userList

    override fun getUserList(page: Long) {
        viewModelScope.launch {
            getAllUserInfoUseCase.invoke(page).collect { result ->
                result.onSuccess {
                    _userList.emit(it)
                }
            }
        }
    }

}