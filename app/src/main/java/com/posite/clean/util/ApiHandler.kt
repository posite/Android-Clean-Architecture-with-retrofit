package com.posite.clean.util

import com.posite.clean.CleanApplication
import com.posite.clean.di.NetworkModule
import retrofit2.Response

suspend fun <T : Any, R : Any> handleApi(
    execute: suspend () -> Response<T>,
    mapper: (T) -> R
): DataResult<R> {
    if (CleanApplication.isOnline().not()) {
        return DataResult.Error(Exception(NetworkModule.NETWORK_EXCEPTION_OFFLINE_CASE))
    }

    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful) {
            body?.let {
                DataResult.Success(mapper(it))
            } ?: run {
                throw NullPointerException(NetworkModule.NETWORK_EXCEPTION_BODY_IS_NULL)
            }
        } else {
            getFailDataResult(body, response)
        }
    } catch (e: Exception) {
        DataResult.Error(e)
    }
}


private fun <T : Any> getFailDataResult(body: T?, response: Response<T>) = body?.let {
    DataResult.Fail(statusCode = response.code(), message = it.toString())
} ?: run {
    DataResult.Fail(statusCode = response.code(), message = response.message())
}