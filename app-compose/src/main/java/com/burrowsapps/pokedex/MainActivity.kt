package com.burrowsapps.pokedex

import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices.PIXEL
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.burrowsapps.pokedex.ui.pokemoninfo.PokemonInfoScreen
import com.burrowsapps.pokedex.ui.pokemonlist.PokemonListScreen
import com.burrowsapps.pokedex.ui.theme.PokedexTheme
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      PokedexTheme {
        MainScreen()
      }
    }
  }
}

@Preview(
  name = "light",
  showBackground = true,
  device = PIXEL,
  locale = "en",
  showSystemUi = true,
  uiMode = UI_MODE_NIGHT_NO,
)
@Preview(
  name = "dark",
  showBackground = true,
  device = PIXEL,
  locale = "en",
  showSystemUi = true,
  uiMode = UI_MODE_NIGHT_YES,
)
@Composable
private fun MainScreenPreview() {
  PokedexTheme {
    MainScreen()
  }
}

@Composable
fun MainScreen() {
  val navController = rememberNavController()

  Scaffold { innerPadding ->
    NavHost(
      modifier = Modifier.padding(innerPadding),
      navController = navController,
      startDestination = Screen.PokemonList.route,
    ) {
      // Pokemon List Screen
      composable(
        route = Screen.PokemonList.route,
      ) {
        PokemonListScreen(
          navController = navController,
        )
      }

      // Pokemon Info Screen
      composable(
        route = Screen.PokemonInfo.route + "/{pokemonName}",
      ) { backStackEntry ->
        PokemonInfoScreen(
          navController = navController,
          pokemon = backStackEntry.arguments?.getString("pokemonName") ?: "bulbasaur",
        )
      }

      // Open Source License Screen
      composable(
        route = Screen.LicenseInfo.route,
      ) {
        val context = LocalContext.current

        LaunchedEffect(Unit) {
          val intent = Intent(context, OssLicensesMenuActivity::class.java)
          context.startActivity(intent)

          // Pop back to the previous screen
          navController.popBackStack()
        }
      }
    }
  }
}

sealed class Screen(val route: String) {
  data object PokemonList : Screen("list")

  data object PokemonInfo : Screen("info")

  data object LicenseInfo : Screen("license")
}
