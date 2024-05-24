package com.burrowsapps.pokedex.ui.pokemoninfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burrowsapps.pokedex.data.api.model.NetworkResult
import com.burrowsapps.pokedex.data.api.model.PokemonInfoResponse
import com.burrowsapps.pokedex.data.repository.PokemonRepository
import com.burrowsapps.pokedex.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PokemonInfoViewModel
  @Inject
  internal constructor(
    private val repository: PokemonRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
  ) : ViewModel() {
    private val _uiState = MutableStateFlow<PokemonInfoUiState>(PokemonInfoUiState.Loading)
    val uiState: StateFlow<PokemonInfoUiState> = _uiState

    fun loadPokemonInfo(name: String) {
      viewModelScope.launch(dispatcher) {
        when (val result = repository.getPokemonInfo(name)) {
          is NetworkResult.Success -> {
            _uiState.value = PokemonInfoUiState.Success(result.data)
          }

          is NetworkResult.Error -> {
            _uiState.value = PokemonInfoUiState.Error(result.message ?: "An unknown error occurred")
          }
          // No Empty or Loading state needed here as those are inherently handled
          is NetworkResult.Empty -> _uiState.value = PokemonInfoUiState.Error("No data found")
          is NetworkResult.Loading -> _uiState.value = PokemonInfoUiState.Loading
        }
      }
    }
  }

sealed class PokemonInfoUiState {
  data object Loading : PokemonInfoUiState()

  data class Success(val response: PokemonInfoResponse?) : PokemonInfoUiState()

  data class Error(val message: String) : PokemonInfoUiState()
}
