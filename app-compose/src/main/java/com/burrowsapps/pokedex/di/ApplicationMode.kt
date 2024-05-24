package com.burrowsapps.pokedex.di

import com.burrowsapps.pokedex.di.ApplicationMode.NORMAL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

enum class ApplicationMode {
  NORMAL,
  TESTING,
}

/** Injections for the API. */
@Module
@InstallIn(SingletonComponent::class)
internal class AppConfigModule {
  @Provides
  @Singleton
  fun provideApplicationMode(): ApplicationMode {
    return NORMAL
  }
}
