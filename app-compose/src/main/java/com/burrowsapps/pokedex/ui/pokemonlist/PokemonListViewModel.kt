package com.burrowsapps.pokedex.ui.pokemonlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.burrowsapps.pokedex.data.api.model.PokemonEntry
import com.burrowsapps.pokedex.data.repository.PokemonRepository
import com.burrowsapps.pokedex.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
internal class PokemonListViewModel
  @Inject
  internal constructor(
    private val repository: PokemonRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
  ) : ViewModel() {
    val pokemonList: Flow<PagingData<PokemonEntry>> =
      Pager(
        PagingConfig(
          pageSize = 20,
          prefetchDistance = 10,
          initialLoadSize = 40,
        ),
      ) {
        PokemonPagingSource(repository, dispatcher)
      }.flow.cachedIn(viewModelScope)
  }
