package com.posite.clean.presentation.ui.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.posite.clean.domain.model.naver.UserInfo
import com.posite.clean.domain.usecase.naver.GetNaverUserInfoUseCase
import com.posite.clean.presentation.base.BaseViewModel
import com.posite.clean.util.DataStoreUtil
import com.posite.clean.util.loginWithKakao
import com.posite.clean.util.onError
import com.posite.clean.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModelImpl @Inject constructor(
    private val dataStoreUtil: DataStoreUtil, private val useCase: GetNaverUserInfoUseCase
) :
    LoginViewModel, BaseViewModel() {
    private val _kakaoEvent: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val kakaoEvent: StateFlow<Boolean>
        get() = _kakaoEvent

    private val _oauthInfo: MutableStateFlow<UserInfo> = MutableStateFlow(UserInfo("", ""))
    override val userInfo: StateFlow<UserInfo>
        get() = _oauthInfo

    private val _loginFinished: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val loginFinished: StateFlow<Boolean>
        get() = _loginFinished

    private val _naverEvent: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val naverEvent: StateFlow<Boolean>
        get() = _naverEvent


    private val _goMain: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val goMain: StateFlow<Boolean>
        get() = _goMain

    override fun checkAutoLogin() {
        viewModelScope.launch {
            val access = dataStoreUtil.loadAccessToken()
            Log.d("accesslogin", access)
            val refresh = dataStoreUtil.loadRefreshToken()
            if (access.isNotBlank() && refresh.isNotBlank()) {
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e("kakao", "사용자 정보 요청 실패", error)
                        viewModelScope.launch {
                            useCase.invoke().collect { result ->
                                result.onSuccess {
                                    _oauthInfo.emit(UserInfo(it.nickname, it.profile))
                                    _loginFinished.value = true
                                }
                            }
                        }
                    } else if (user != null) {
                        viewModelScope.launch {
                            Log.d("kakao", "id: ${user.id}, email: ${user.kakaoAccount?.email}")
                            _oauthInfo.emit(
                                UserInfo(
                                    user.kakaoAccount?.profile?.nickname!!,
                                    user.kakaoAccount?.profile?.thumbnailImageUrl!!
                                )
                            )
                        }
                        _loginFinished.value = true
                    }
                }
            }
        }
    }

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
                            _oauthInfo.emit(
                                UserInfo(
                                    user.kakaoAccount?.profile?.nickname!!,
                                    user.kakaoAccount?.profile?.thumbnailImageUrl!!
                                )
                            )
                            Log.d(
                                "real",
                                "access: ${token.accessToken}, refresh: ${token.refreshToken}"
                            )
                            dataStoreUtil.saveRefreshToken(token.refreshToken)
                            dataStoreUtil.saveAccessToken(token.accessToken)
                            Log.d(
                                "now",
                                "access: ${dataStoreUtil.loadAccessToken()}, refresh: ${dataStoreUtil.loadRefreshToken()}"
                            )
                            //연결 끊기 : 앱과 카카오 연결 끊기(성공 시 로그아웃 처리도 됨)
                            /*
                            UserApiClient.instance.unlink { error ->
                                if (error != null) {
                                    Log.e("kakao", "연결 끊기 실패", error)
                                } else {
                                    Log.i("kakao", "연결 끊기 성공. SDK에서 토큰 삭제 됨")
                                }
                            }
                            */
                        }
                        _loginFinished.value = true
                    }
                }
            }
            UserApiClient.loginWithKakao(context, callback)
        }
    }

    override fun onNaverClick() {
        viewModelScope.launch {
            _naverEvent.emit(true)
        }
    }

    override fun getNaverToken(context: Context) {
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                Log.d("naver", NaverIdLoginSDK.getState().toString())
                viewModelScope.launch {
                    dataStoreUtil.saveAccessToken(NaverIdLoginSDK.getAccessToken()!!)
                    dataStoreUtil.saveRefreshToken(NaverIdLoginSDK.getRefreshToken()!!)
                    useCase.invoke().collect { result ->
                        result.onSuccess {
                            _oauthInfo.emit(it)
                        }.onError {
                            Log.d("naver", it.toString())
                        }
                    }
                    _loginFinished.value = true
                }
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(
                    context,
                    "errorCode:$errorCode, errorDesc:$errorDescription",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }


        NaverIdLoginSDK.authenticate(context, oauthLoginCallback)
    }

    override fun goMainClick() {
        viewModelScope.launch {
            _goMain.emit(true)
        }
    }
}