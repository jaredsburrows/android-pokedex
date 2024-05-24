package com.burrowsapps.pokedex.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.burrowsapps.pokedex.data.api.PokemonService
import com.burrowsapps.pokedex.data.api.model.PokemonInfoResponse
import com.burrowsapps.pokedex.data.api.model.PokemonListResponse
import com.burrowsapps.pokedex.di.TestApiConfigModule
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol.HTTP_1_1
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Response
import java.net.HttpURLConnection.HTTP_INTERNAL_ERROR

@RunWith(AndroidJUnit4::class)
class PokemonRepositoryTest {
  private val service = mock<PokemonService>()
  private val offset = 0
  private val limit = 0
  private val name = ""
  private val listResponse = PokemonListResponse()
  private val infoResponse = PokemonInfoResponse()

  private lateinit var sut: PokemonRepository

  @Before
  fun setUp() {
    sut = PokemonRepository(service)
  }

  @Test
  fun testFetchingPokemonListSuccess() = runTest {
    whenever(service.fetchPokemonList(eq(offset), eq(limit)))
      .thenReturn(Response.success(listResponse))

    val result = sut.getPokemonList(offset, limit).data

    verify(service).fetchPokemonList(eq(offset), eq(limit))
    assertThat(result).isEqualTo(listResponse)
  }

  @Test
  fun testFetchingPokemonListError() = runTest {
    val errorResponse = okhttp3.Response.Builder()
      .code(HTTP_INTERNAL_ERROR)
      .message("Broken!")
      .protocol(HTTP_1_1)
      .request(okhttp3.Request.Builder().url(TestApiConfigModule().provideBaseUrl()).build())
      .build()
    val plainText = "text/plain; charset=utf-8".toMediaType()
    val errorBody = "Broken!".toResponseBody(plainText)
    whenever(service.fetchPokemonList(eq(offset), eq(limit)))
      .thenReturn(Response.error(errorBody, errorResponse))

    val result = sut.getPokemonList(offset, limit).data

    verify(service).fetchPokemonList(eq(offset), eq(limit))
    assertThat(result).isNull()
  }

  @Test
  fun testFetchingPokemonInfoSuccess() = runTest {
    whenever(service.fetchPokemonInfo(eq(name)))
      .thenReturn(Response.success(infoResponse))

    val result = sut.getPokemonInfo(name = name).data

    verify(service).fetchPokemonInfo(eq(name))
    assertThat(result).isEqualTo(infoResponse)
  }

  @Test
  fun testLoadSearchImagesError() = runTest {
    val errorResponse = okhttp3.Response.Builder()
      .code(HTTP_INTERNAL_ERROR)
      .message("Broken!")
      .protocol(HTTP_1_1)
      .request(okhttp3.Request.Builder().url(TestApiConfigModule().provideBaseUrl()).build())
      .build()
    val plainText = "text/plain; charset=utf-8".toMediaType()
    val errorBody = "Broken!".toResponseBody(plainText)
    whenever(service.fetchPokemonInfo(eq(name)))
      .thenReturn(Response.error(errorBody, errorResponse))

    val result = sut.getPokemonInfo(name).data

    verify(service).fetchPokemonInfo(eq(name))
    assertThat(result).isNull()
  }
}
