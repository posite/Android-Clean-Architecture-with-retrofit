package com.posite.clean.domain.usecase.test

import com.posite.clean.domain.model.test.UserList
import com.posite.clean.domain.repository.test.UserInfoRepository
import com.posite.clean.util.DataResult
import com.posite.clean.util.onError
import com.posite.clean.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllUserInfoUseCase @Inject constructor(private val repository: UserInfoRepository) {
    suspend operator fun invoke(page: Long) = flow<DataResult<UserList>> {
        try {
            emit(repository.getAllUserInfo(page).onSuccess {

            }.onError {

            })
        } catch (e: Exception) {

        }
    }
}