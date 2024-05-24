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

  class Error<T>(data: T? = null, message: String? = null) :
    NetworkResult<T>(data = data, message = message)

  companion object {
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
      return runCatching { apiCall() }
        .fold(
          onSuccess = { response ->
            val body = response.body()
            when {
              // Success response with empty body
              response.isSuccessful && body == null -> {
                Timber.i("Request was successful with an empty body")
                Empty()
              }
              // Success response with body
              response.isSuccessful && body != null -> {
                Timber.i("Request was successful with a body")
                Success(data = body)
              }
              else -> {
                // Error response
                Timber.e("Request failed with message: ${response.message()}")
                error(response.message())
              }
            }
          },
          onFailure = { e ->
            // Error response because of exception
            Timber.e(e, "Request failed with exception")
            error(e.message ?: e.toString())
          },
        )
    }

    private fun <T> error(errorMessage: String): NetworkResult<T> = Error(message = errorMessage)
  }
}
