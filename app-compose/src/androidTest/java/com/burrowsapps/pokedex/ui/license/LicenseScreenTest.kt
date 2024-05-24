package com.burrowsapps.pokedex.ui.license

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.burrowsapps.pokedex.MainActivity
import com.burrowsapps.pokedex.R
import com.burrowsapps.pokedex.di.ApiConfigModule
import com.burrowsapps.pokedex.test.TestFileUtils
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
class LicenseScreenTest {
  @get:Rule(order = 0)
  internal val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1)
  internal val composeTestRule = createAndroidComposeRule<MainActivity>()

  @Inject
  @ApplicationContext
  internal lateinit var context: Context

  private val pokemonListScreenTitle by lazy { context.getString(R.string.pokemon_list_screen_title) }
  private val licenseScreenTitle by lazy { context.getString(R.string.license_screen_title) }
  private val menuMore by lazy { context.getString(R.string.menu_more) }

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
                    endsWith(".png") || endsWith(".gif") ->
                      getMockImageResponse(
                        fileName = "/ic_launcher.webp",
                      )

                    else -> MockResponse().setResponseCode(code = HTTP_NOT_FOUND)
                  }
                }
              }
            }

          start(TestFileUtils.MOCK_SERVER_PORT)
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
  }

  @Test
  fun testLicenseScreenTitleIsShowing() {
    openLicenseScreen()

    onView(withText("Debug License Info")).check(matches(isDisplayed())) // 'licenseScreenTitle' is in production
  }

  @Test
  fun testGoBackViaHardwareBackButton() {
    openLicenseScreen()

    pressBack()

    composeTestRule.onNodeWithText(text = pokemonListScreenTitle).assertIsDisplayed()
  }

  private fun openLicenseScreen() {
    composeTestRule.onNodeWithContentDescription(label = menuMore).performClick()
    composeTestRule.waitForIdle()

    composeTestRule.onNodeWithText(text = licenseScreenTitle).performClick()
    composeTestRule.waitForIdle()
  }
}
