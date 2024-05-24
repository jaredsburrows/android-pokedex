package com.burrowsapps.pokedex.ui.pokemonlist

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToIndex
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.burrowsapps.pokedex.MainActivity
import com.burrowsapps.pokedex.R
import com.burrowsapps.pokedex.di.ApiConfigModule
import com.burrowsapps.pokedex.test.TestFileUtils.MOCK_SERVER_PORT
import com.burrowsapps.pokedex.test.TestFileUtils.getMockImageResponse
import com.burrowsapps.pokedex.test.TestFileUtils.getMockResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
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
class PokemonListScreenTest {
  @get:Rule(order = 0)
  internal val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1)
  internal val composeTestRule = createAndroidComposeRule<MainActivity>()

  @Inject
  @ApplicationContext
  internal lateinit var context: Context

  private val pokemonListScreenTitle by lazy { context.getString(R.string.pokemon_list_screen_title) }

  @Before
  fun setUp() {
    hiltRule.inject()
  }

  companion object {
    private lateinit var webServer: MockWebServer

    @BeforeClass
    @JvmStatic
    fun startMockServer() {
      webServer =
        MockWebServer().apply {
          dispatcher =
            object : Dispatcher() {
              override fun dispatch(request: RecordedRequest): MockResponse {
                request.path.orEmpty().apply {
                  return when {
                    // Pokemon List Screen
                    // Match https://pokeapi.co/api/v2/pokemon?offset=<any number>&limit=<any number>
                    matches(Regex("^/pokemon/?\\?offset=\\d+&limit=\\d+$")) ->
                      getMockResponse(
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

    @AfterClass
    @JvmStatic
    fun shutDownServer() {
      webServer.shutdown()
    }
  }

  @Test
  fun testTitleIsShowing() {
    composeTestRule.onNodeWithText(text = pokemonListScreenTitle).assertIsDisplayed()
    composeTestRule.waitForIdle()
  }

  @Test
  fun testListIsShowing() {
    composeTestRule.onNodeWithText(text = "Bulbasaur").assertIsDisplayed()
    composeTestRule.waitForIdle()
  }

  @Test
  fun testListCanScroll() {
    composeTestRule.onNodeWithContentDescription("List").performScrollToIndex(10)
    composeTestRule.waitForIdle()

    composeTestRule.onNodeWithText(text = "Metapod").assertIsDisplayed() // 10th pokemon
    composeTestRule.waitForIdle()

    composeTestRule.onNodeWithContentDescription("List").performScrollToIndex(10)
    composeTestRule.waitForIdle()
  }
}
