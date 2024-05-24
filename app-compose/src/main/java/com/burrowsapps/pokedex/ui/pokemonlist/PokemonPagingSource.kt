package com.burrowsapps.pokedex.ui.pokemonlist

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.burrowsapps.pokedex.data.api.PokemonService
import com.burrowsapps.pokedex.data.api.model.NetworkResult
import com.burrowsapps.pokedex.data.api.model.PokemonEntry
import com.burrowsapps.pokedex.data.repository.PokemonRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class PokemonPagingSource(
  private val repository: PokemonRepository,
  private val dispatcher: CoroutineDispatcher,
) : PagingSource<Int, PokemonEntry>() {
  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonEntry> =
    withContext(dispatcher) {
      val position = params.key ?: 0

      try {
        when (val response = repository.getPokemonList(position, PokemonService.LIMIT)) {
          is NetworkResult.Success -> {
            val pokemonList = response.data?.results ?: emptyList()
            val nextKey = if (pokemonList.isEmpty()) null else position + PokemonService.LIMIT

            Timber.i("PagingSource Position: $position, NextKey: $nextKey, Items Loaded: ${pokemonList.size}")

            LoadResult.Page(
              data = pokemonList,
              prevKey = if (position == 0) null else position - PokemonService.LIMIT,
              nextKey = nextKey,
            )
          }
          is NetworkResult.Error -> LoadResult.Error(Exception(response.message))
          else -> LoadResult.Error(Exception("Unknown error"))
        }
      } catch (exception: Exception) {
        LoadResult.Error(exception)
      }
    }

  override fun getRefreshKey(state: PagingState<Int, PokemonEntry>): Int? {
    return state.anchorPosition?.let { anchorPosition ->
      state.closestPageToPosition(anchorPosition)?.let { closestPage ->
        closestPage.prevKey?.plus(PokemonService.LIMIT) ?: closestPage.nextKey?.minus(PokemonService.LIMIT)
      }
    }
  }
}
