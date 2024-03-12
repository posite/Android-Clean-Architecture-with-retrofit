package com.posite.clean.domain.usecase.naver

import com.posite.clean.domain.model.naver.UserInfo
import com.posite.clean.domain.repository.naver.NaverUserInfoRepository
import com.posite.clean.util.DataResult
import com.posite.clean.util.onError
import com.posite.clean.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetNaverUserInfoUseCase @Inject constructor(private val repository: NaverUserInfoRepository) {
    suspend operator fun invoke() = flow<DataResult<UserInfo>> {
        try {
            emit(repository.getNaverUserInfo().onSuccess {

            }.onError {

            })
        } catch (e: Exception) {

        }
    }
}