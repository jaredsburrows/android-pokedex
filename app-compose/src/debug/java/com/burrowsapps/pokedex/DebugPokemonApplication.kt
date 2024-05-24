package com.burrowsapps.pokedex

import android.os.StrictMode
import timber.log.Timber

open class DebugPokemonApplication : PokemonApplication() {
  override fun onCreate() {
    super.onCreate()

    Timber.plant(Timber.DebugTree())

    StrictMode.setThreadPolicy(
      StrictMode.ThreadPolicy.Builder()
        .detectAll()
        .penaltyLog()
        .build(),
    )
    StrictMode.setVmPolicy(
      StrictMode.VmPolicy.Builder()
        .detectAll()
        .penaltyLog()
        .build(),
    )
  }
}
