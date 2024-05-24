package com.burrowsapps.pokedex

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.setCompatVectorFromResourcesEnabled
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class PokemonApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
    setCompatVectorFromResourcesEnabled(true)
  }
}
