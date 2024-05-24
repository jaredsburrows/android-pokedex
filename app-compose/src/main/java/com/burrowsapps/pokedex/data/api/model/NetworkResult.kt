package com.burrowsapps.pokedex.data.api.model

import retrofit2.Response
import timber.log.Timber

sealed class NetworkResult<T>(
  val data: T? = null,
  val message: String? = null,
) {
  class Loading<T> : NetworkResult<T>()

  class Success<T>(data: T) : NetworkResult<T>(data = data)

  class Empty<T> : NetworkResult<T>()

  class Error<T>(data: T? = null, message: String? = null) : NetworkResult<T>(
    data = data,
    message = message,
  )

  companion object {
    private const val ERROR_MESSAGE_PREFIX = "Api call failed"

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
      return try {
        val response = apiCall()
        val body = response.body()
        if (response.isSuccessful && body != null) {
          return Success(data = body)
        }
        Timber.e("request failed")
        error(errorMessage = "$ERROR_MESSAGE_PREFIX ${response.code()} ${response.message()}")
      } catch (e: Exception) {
        Timber.e(t = e, message = "request failed")
        error(errorMessage = e.message ?: e.toString())
      }
    }

    private fun <T> error(errorMessage: String): NetworkResult<T> = Error(data = null, message = "$ERROR_MESSAGE_PREFIX $errorMessage")
  }
}
