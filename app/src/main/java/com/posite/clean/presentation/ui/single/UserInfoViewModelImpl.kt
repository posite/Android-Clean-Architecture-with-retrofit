package com.posite.clean.presentation.ui.single

import androidx.lifecycle.viewModelScope
import com.posite.clean.data.dto.user_list.UserDto
import com.posite.clean.domain.usecase.GetSingleUserInfoUseCase
import com.posite.clean.presentation.base.BaseViewModel
import com.posite.clean.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModelImpl @Inject constructor(private val getSingleUserInfoUseCase: GetSingleUserInfoUseCase) :
    BaseViewModel(), UserInfoViewModel {
    private val _userData = MutableStateFlow<UserDto>(UserDto("", "", "", 0, ""))
    override val userData: StateFlow<UserDto>
        get() = _userData

    override fun getSingeUserInfo(id: Long) {
        viewModelScope.launch {
            getSingleUserInfoUseCase.invoke(id).collect { result ->
                result.onSuccess {
                    _userData.emit(it)
                }
            }
        }
    }
}