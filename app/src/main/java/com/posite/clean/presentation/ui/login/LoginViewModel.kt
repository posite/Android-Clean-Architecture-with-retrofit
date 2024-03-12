package com.posite.clean.presentation.ui.login

import android.content.Context
import com.posite.clean.domain.model.naver.UserInfo
import kotlinx.coroutines.flow.StateFlow

interface LoginViewModel {
    val kakaoEvent: StateFlow<Boolean>
    val naverEvent: StateFlow<Boolean>
    val userInfo: StateFlow<UserInfo>
    val loginFinished: StateFlow<Boolean>

    val goMain: StateFlow<Boolean>

    fun checkAutoLogin()
    fun onKakaoClick()
    fun getKakaoToken(context: Context)

    fun onNaverClick()
    fun getNaverToken(context: Context)

    fun goMainClick()
}