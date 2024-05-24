@file:OptIn(ExperimentalCoroutinesApi::class)

package com.burrowsapps.pokedex.ui.pokemoninfo

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.burrowsapps.pokedex.data.api.model.NetworkResult
import com.burrowsapps.pokedex.data.api.model.PokemonInfoResponse
import com.burrowsapps.pokedex.data.repository.PokemonRepository
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PokemonInfoViewModelTest {
  private val repository = mock<PokemonRepository>()
  private val testDispatcher = UnconfinedTestDispatcher()
  private val name = ""
  private val infoResponse = PokemonInfoResponse()

  private lateinit var sut: PokemonInfoViewModel

  @Before
  fun setUp() {
    sut = PokemonInfoViewModel(repository, testDispatcher)
  }

  @Test
  fun testFetchingPokemonInfoSuccess() = runTest {
    whenever(repository.getPokemonInfo(eq(name)))
      .thenReturn(NetworkResult.Success(infoResponse))

    sut.loadPokemonInfo(name)
    val result = sut.uiState.value

    verify(repository).getPokemonInfo(eq(name))
    assertThat(result).isEqualTo(PokemonInfoUiState.Success(infoResponse))
  }

  @Test
  fun testFetchingPokemonInfoError() = runTest {
    whenever(repository.getPokemonInfo(eq(name)))
      .thenReturn(NetworkResult.Error(message = "Broken!"))

    sut.loadPokemonInfo(name)
    val result = sut.uiState.value

    verify(repository).getPokemonInfo(eq(name))
    assertThat(result).isEqualTo(PokemonInfoUiState.Error("Broken!"))
  }
}
