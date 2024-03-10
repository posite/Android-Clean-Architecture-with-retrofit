package com.posite.clean.presentation.ui.login

import android.content.Context
import com.posite.clean.data.dto.kakao.KakaoInfo
import kotlinx.coroutines.flow.StateFlow

interface LoginViewModel {
    val kakaoEvent: StateFlow<Boolean>
    val kakaoInfo: StateFlow<KakaoInfo>
    val kakaoLoginFinished: StateFlow<Boolean>
    val goMain: StateFlow<Boolean>

    fun onKakaoClick()

    fun getKakaoToken(context: Context)

    fun goMainClick()
}