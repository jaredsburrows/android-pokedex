@file:OptIn(ExperimentalCoroutinesApi::class)

package com.burrowsapps.pokedex.ui.pokemonlist

import androidx.paging.PagingData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.burrowsapps.pokedex.data.api.model.NetworkResult
import com.burrowsapps.pokedex.data.api.model.PokemonEntry
import com.burrowsapps.pokedex.data.api.model.PokemonListResponse
import com.burrowsapps.pokedex.data.repository.PokemonRepository
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PokemonListViewModelTest {
  private val repository = mock<PokemonRepository>()
  private val testDispatcher = StandardTestDispatcher()
  private val offset = 20
  private val limit = 40
  private val listResponse = PokemonListResponse()

  private lateinit var sut: PokemonListViewModel

  @Before
  fun setUp() {
    sut = PokemonListViewModel(repository, testDispatcher)
  }

  @Test
  fun testFetchingPokemonListSuccess() = runTest {
    val expected = PagingData.from(listOf<PokemonEntry>())

    whenever(repository.getPokemonList(eq(offset), eq(limit)))
      .thenReturn(NetworkResult.Success(listResponse))

    val flow = sut.pokemonList
    val job = launch {
      flow.collect { pagingData ->
        assertThat(pagingData).isEqualTo(expected)
      }
    }

    job.cancel()
  }

  @Test
  fun testFetchingPokemonListError() = runTest {
    whenever(repository.getPokemonList(eq(offset), eq(limit)))
      .thenReturn(NetworkResult.Error(message = "Broken!"))

    val flow = sut.pokemonList
    val job = launch {
      flow.collect { pagingData ->
        assertThat(pagingData.toString()).isEmpty()
      }
    }

    job.cancel()
  }
}
