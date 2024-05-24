package com.burrowsapps.pokedex.data.api

import com.burrowsapps.pokedex.data.api.model.PokemonInfoResponse
import com.burrowsapps.pokedex.data.api.model.PokemonListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// base url - https://pokeapi.co/api/v2/
interface PokemonService {
  // https://pokeapi.co/
  // example: https://pokeapi.co/api/v2/pokemon?offset=0&limit=20
  @GET("pokemon")
  suspend fun fetchPokemonList(
    @Query("offset") offset: Int,
    @Query("limit") limit: Int = LIMIT,
  ): Response<PokemonListResponse>

  // https://pokeapi.co/
  // example: https://pokeapi.co/api/v2/pokemon/ditto
  @GET("pokemon/{name}")
  suspend fun fetchPokemonInfo(
    @Path("name") name: String?,
  ): Response<PokemonInfoResponse>

  companion object {
    const val BASE_URL = "https://pokeapi.co/api/v2/"
    const val LIMIT = 20
  }
}
