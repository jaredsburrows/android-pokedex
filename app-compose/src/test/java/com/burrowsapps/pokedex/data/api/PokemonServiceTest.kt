package com.burrowsapps.pokedex.data.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.burrowsapps.pokedex.di.ApiConfigModule
import com.burrowsapps.pokedex.test.TestFileUtils.MOCK_SERVER_PORT
import com.burrowsapps.pokedex.test.TestFileUtils.getMockImageResponse
import com.burrowsapps.pokedex.test.TestFileUtils.getMockResponse
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(ApiConfigModule::class)
@Config(application = HiltTestApplication::class)
@RunWith(AndroidJUnit4::class)
class PokemonServiceTest {
  @get:Rule(order = 0)
  internal val hiltRule = HiltAndroidRule(this)

  @Inject
  internal lateinit var sut: PokemonService

  private val server = MockWebServer()

  @Before
  fun setUp() {
    hiltRule.inject()

    server.apply {
      dispatcher =
        object : Dispatcher() {
          override fun dispatch(request: RecordedRequest): MockResponse {
            request.path.orEmpty().apply {
              return when {
                // Pokemon List Screen
                // Match https://pokeapi.co/api/v2/pokemon?offset=<any number>&limit=<any number>
                matches(Regex("^/pokemon/?\\?offset=\\d+&limit=\\d+$")) -> getMockResponse(
                  fileName = "/pokemon_list.json",
                )

                // Pokemon Info Screen
                // Match https://pokeapi.co/api/v2/pokemon/<any string>
                matches(Regex("^/pokemon/[^/]+/?$")) -> getMockResponse(fileName = "/pokemon_info.json")

                // Handling image files with specific response
                endsWith(".png") || endsWith(".gif") -> getMockImageResponse(fileName = "/ic_launcher.webp")
                else -> MockResponse().setResponseCode(code = HTTP_NOT_FOUND)
              }
            }
          }
        }

      start(MOCK_SERVER_PORT)
    }
  }

  @After
  fun tearDown() {
    server.shutdown()
  }

  @Test
  fun testFetchPokemonListURLShouldParseCorrectly() = runTest {
    val response = sut.fetchPokemonList(0, 20)
    val body = response.body()!!

    // Should be Bulbasaur
    assertThat(body.results.first().name).matches("bulbasaur")
    assertThat(body.results.first().url).matches("http.*localhost.*api/v2/pokemon/1/")
  }

  @Test
  fun testFetchPokemonInfoURLShouldParseCorrectly() = runTest {
    val pokemonName = "bulbasaur"
    val response = sut.fetchPokemonInfo(pokemonName)
    val body = response.body()!!

    assertThat(body.name).matches(pokemonName)
  }
}
