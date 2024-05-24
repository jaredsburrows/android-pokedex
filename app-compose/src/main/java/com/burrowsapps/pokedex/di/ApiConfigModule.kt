package com.burrowsapps.pokedex.di

import com.burrowsapps.pokedex.data.api.PokemonService.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Injections for the API. */
@Module
@InstallIn(SingletonComponent::class)
class ApiConfigModule {
  @Provides
  @Singleton
  fun provideBaseUrl(): String {
    return BASE_URL
  }
}
