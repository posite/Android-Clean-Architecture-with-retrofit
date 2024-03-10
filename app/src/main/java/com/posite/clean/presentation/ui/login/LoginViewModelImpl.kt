package com.posite.clean.presentation.ui.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.posite.clean.data.dto.kakao.KakaoInfo
import com.posite.clean.presentation.base.BaseViewModel
import com.posite.clean.util.DataStoreUtil
import com.posite.clean.util.loginWithKakao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModelImpl @Inject constructor(
    private val dataStoreUtil: DataStoreUtil
) :
    LoginViewModel, BaseViewModel() {
    private val _kakaoEvent: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val kakaoEvent: StateFlow<Boolean>
        get() = _kakaoEvent

    private val _kakaoInfo: MutableStateFlow<KakaoInfo> = MutableStateFlow(KakaoInfo("", ""))
    override val kakaoInfo: StateFlow<KakaoInfo>
        get() = _kakaoInfo

    private val _kakaoLoginFinished: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val kakaoLoginFinished: StateFlow<Boolean>
        get() = _kakaoLoginFinished

    private val _goMain: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val goMain: StateFlow<Boolean>
        get() = _goMain

    override fun onKakaoClick() {
        viewModelScope.launch {
            _kakaoEvent.emit(_kakaoEvent.value.not())
        }
    }

    override fun getKakaoToken(context: Context) {
        viewModelScope.launch {
            val callback: (OAuthToken) -> Unit = { token ->
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e("kakao", "사용자 정보 요청 실패", error)
                    } else if (user != null) {
                        viewModelScope.launch {
                            Log.d("kakao", "id: ${user.id}, email: ${user.kakaoAccount?.email}")
                            _kakaoInfo.emit(
                                KakaoInfo(
                                    user.kakaoAccount?.profile?.nickname!!,
                                    user.kakaoAccount?.profile?.thumbnailImageUrl!!
                                )
                            )
                            dataStoreUtil.saveRefreshToken(token.refreshToken)
                            dataStoreUtil.saveAccessToken(token.accessToken)

                            //연결 끊기 : 앱과 카카오 연결 끊기(성공 시 로그아웃 처리도 됨)
                            UserApiClient.instance.unlink { error ->
                                if (error != null) {
                                    Log.e("kakao", "연결 끊기 실패", error)
                                } else {
                                    Log.i("kakao", "연결 끊기 성공. SDK에서 토큰 삭제 됨")
                                }
                            }
                        }
                        _kakaoLoginFinished.value = true
                    }
                }
            }
            UserApiClient.loginWithKakao(context, callback)
        }
    }

    override fun goMainClick() {
        viewModelScope.launch {
            _goMain.emit(true)
        }
    }
}