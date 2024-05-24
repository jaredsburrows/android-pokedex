package com.burrowsapps.pokedex.data.api

import com.burrowsapps.pokedex.data.api.model.PokemonInfoResponse
import com.burrowsapps.pokedex.data.api.model.PokemonListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Pokemon API Service for fetching Pokemon list and info.
 *
 * Base URL: https://pokeapi.co/api/v2/
 */
interface PokemonService {
  /**
   * Fetches a list of Pokemon.
   *
   * Example: https://pokeapi.co/api/v2/pokemon?offset=0&limit=20
   *
   * @param offset The starting position of the results.
   * @param limit The number of results to return (default: 20).
   * @return Response containing a list of Pokemon.
   */
  @GET("pokemon")
  suspend fun fetchPokemonList(
    @Query("offset") offset: Int,
    @Query("limit") limit: Int = LIMIT,
  ): Response<PokemonListResponse>

  /**
   * Fetches information about a specific Pokemon by name.
   *
   * Example: https://pokeapi.co/api/v2/pokemon/ditto
   *
   * @param name The name of the Pokemon.
   * @return Response containing information about the specified Pokemon.
   */
  @GET("pokemon/{name}")
  suspend fun fetchPokemonInfo(
    @Path("name") name: String,
  ): Response<PokemonInfoResponse>

  companion object {
    const val BASE_URL = "https://pokeapi.co/api/v2/"
    const val LIMIT = 20
  }
}
