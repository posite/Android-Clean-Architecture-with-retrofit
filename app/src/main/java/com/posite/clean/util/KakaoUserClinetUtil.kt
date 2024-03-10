package com.posite.clean.util

import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun UserApiClient.Companion.loginWithKakao(
    context: Context, callback: (OAuthToken) -> Unit
) {
    if (instance.isKakaoTalkLoginAvailable(context)) {
        Log.d("kakao", "가능")
        try {
            UserApiClient.loginWithKakaoTalk(context, callback)
        } catch (error: Throwable) {
            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) throw error
            UserApiClient.loginWithKakaoAccount(context, callback)
        }
    } else {
        Log.d("kakao", "불가능")
        UserApiClient.loginWithKakaoAccount(context, callback)
    }
}

/**
 * 카카오톡으로 로그인 시도
 */
suspend fun UserApiClient.Companion.loginWithKakaoTalk(
    context: Context,
    callback: (OAuthToken) -> Unit
) {
    suspendCoroutine<OAuthToken> { continuation ->
        instance.loginWithKakaoTalk(context) { token, error ->
            if (error != null) {
                Log.d("kakao", "카카오톡 에러 ${error.message}")
                continuation.resumeWithException(error)
            } else if (token != null) {
                Log.d("kakao", "카카오톡으로 로그인 성공")
                continuation.resume(token)
                callback(token)
            } else {
                continuation.resumeWithException(RuntimeException("kakao access token을 받아오는데 실패함, 이유는 명확하지 않음."))
            }
        }
    }
}

/**
 * 카카오 계정으로 로그인 시도
 */
suspend fun UserApiClient.Companion.loginWithKakaoAccount(
    context: Context,
    callback: (OAuthToken) -> Unit
) {
    suspendCoroutine<OAuthToken> { continuation ->
        instance.loginWithKakaoAccount(context) { token, error ->
            if (error != null) {
                continuation.resumeWithException(error)
            } else if (token != null) {
                Log.d("kakao", "카카오계정으로 로그인 성공!!")
                continuation.resume(token)
                callback(token)
            } else {
                continuation.resumeWithException(RuntimeException("kakao access token을 받아오는데 실패함, 이유는 명확하지 않음."))
            }
        }
    }
}