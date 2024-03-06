package com.posite.clean.domain.usecase

import com.posite.clean.data.dto.user_list.UserDto
import com.posite.clean.domain.repository.UserInfoRepository
import com.posite.clean.util.DataResult
import com.posite.clean.util.onError
import com.posite.clean.util.onSuccess
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSingleUserInfoUseCase @Inject constructor(private val repository: UserInfoRepository) {
    suspend operator fun invoke(id: Long) = flow<DataResult<UserDto>> {
        try {
            emit(repository.getSingleUserInfo(id).onSuccess {
                
            }.onError {

            })
        } catch (e: Exception) {

        }
    }
}