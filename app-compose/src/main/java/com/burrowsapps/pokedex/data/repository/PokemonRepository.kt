package com.burrowsapps.pokedex.data.repository

import com.burrowsapps.pokedex.data.api.PokemonService
import com.burrowsapps.pokedex.data.api.model.NetworkResult
import com.burrowsapps.pokedex.data.api.model.NetworkResult.Companion.safeApiCall
import com.burrowsapps.pokedex.data.api.model.PokemonInfoResponse
import com.burrowsapps.pokedex.data.api.model.PokemonListResponse
import javax.inject.Inject

// TODO data breakdown
// from DTOs to domain models
class PokemonRepository
  @Inject
  internal constructor(
    private val service: PokemonService,
  ) {
    suspend fun getPokemonList(
      offset: Int,
      limit: Int,
    ): NetworkResult<PokemonListResponse> = safeApiCall { service.fetchPokemonList(offset, limit) }

    suspend fun getPokemonInfo(name: String): NetworkResult<PokemonInfoResponse> = safeApiCall { service.fetchPokemonInfo(name) }
  }
