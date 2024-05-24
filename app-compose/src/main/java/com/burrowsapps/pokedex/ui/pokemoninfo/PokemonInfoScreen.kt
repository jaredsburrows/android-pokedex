@file:OptIn(ExperimentalMaterial3Api::class)

package com.burrowsapps.pokedex.ui.pokemoninfo

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.burrowsapps.pokedex.R
import com.burrowsapps.pokedex.data.api.model.PokemonAbility
import com.burrowsapps.pokedex.data.api.model.PokemonForm
import com.burrowsapps.pokedex.data.api.model.PokemonInfoResponse
import com.burrowsapps.pokedex.data.api.model.PokemonStat
import com.burrowsapps.pokedex.ui.common.PokemonGlideImage
import com.burrowsapps.pokedex.ui.common.TheToolbar
import com.burrowsapps.pokedex.ui.theme.PokedexTheme
import java.util.Locale

@Preview(
  name = "light",
  showBackground = true,
  device = Devices.PIXEL,
  locale = "en",
  showSystemUi = true,
  uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
  name = "dark",
  showBackground = true,
  device = Devices.PIXEL,
  locale = "en",
  showSystemUi = true,
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun PokemonInfoScreenPreview(navController: NavHostController = rememberNavController()) {
  PokedexTheme {
    PokemonInfoScreen(navController)
  }
}

@Composable
internal fun PokemonInfoScreen(
  navController: NavHostController,
  pokemon: String = "bulbasaur",
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
  val viewModel = hiltViewModel<PokemonInfoViewModel>()
  viewModel.loadPokemonInfo(pokemon)

  Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      val title = stringResource(id = R.string.pokemon_info_screen_title)
      val pokemonName =
        pokemon.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ENGLISH) else it.toString() }
      val screenName = "$title: $pokemonName"

      TheToolbar(
        navController = navController,
        scrollBehavior = scrollBehavior,
        screenName = screenName,
      )
    },
  ) { paddingValues ->
    val uiState = viewModel.uiState.collectAsState()

    TheContent(
      navController = navController,
      innerPadding = paddingValues,
      uiState = uiState.value,
    )
  }
}

@Composable
private fun TheContent(
  navController: NavHostController,
  innerPadding: PaddingValues,
  uiState: PokemonInfoUiState,
) {
  Column(modifier = Modifier.padding(innerPadding)) {
    when (uiState) {
      is PokemonInfoUiState.Loading -> {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
          CircularProgressIndicator()
        }
      }

      is PokemonInfoUiState.Success -> {
        PokemonProfile(pokemonInfo = uiState.response!!)
      }

      is PokemonInfoUiState.Error -> {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
          Text(
            text = "Error: ${uiState.message}",
//            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
          )
        }
      }
    }
  }
}

@Composable
fun PokemonProfile(pokemonInfo: PokemonInfoResponse) {
  Column(
    modifier =
    Modifier
        .fillMaxSize()
        .padding(16.dp)
        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp))
        .semantics { contentDescription = "Icon" },
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Spacer(modifier = Modifier.height(16.dp))
    PokemonGlideImage(imageUrl = pokemonInfo.sprites.other.officialArtwork.frontDefault, size = 150)
    PropertySection(title = "Number", value = pokemonInfo.id.toString())
    PropertySection(title = "Base Experience", value = pokemonInfo.baseExperience.toString())
    PropertySection(title = "Height", value = "${pokemonInfo.height}")
    PropertySection(title = "Weight", value = "${pokemonInfo.weight}")
    AbilitiesList(abilities = pokemonInfo.abilities)
    FormsList(forms = pokemonInfo.forms)
    StatsList(stats = pokemonInfo.stats)
    Spacer(modifier = Modifier.height(16.dp))
  }
}

@Composable
fun AbilitiesList(abilities: List<PokemonAbility>) {
  Column(modifier = Modifier.padding(8.dp)) {
    Text("Abilities:", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    abilities.forEach { ability ->
      Text(
        " - ${ability.ability.name} (Hidden: ${ability.isHidden})",
        style = MaterialTheme.typography.bodyMedium,
      )
    }
  }
}

@Composable
fun FormsList(forms: List<PokemonForm>) {
  Column(modifier = Modifier.padding(8.dp)) {
    Text("Forms:", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    forms.forEach { form ->
      Text(" - ${form.name}", style = MaterialTheme.typography.bodyMedium)
    }
  }
}

@Composable
fun StatsList(stats: List<PokemonStat>) {
  Column(modifier = Modifier.padding(8.dp)) {
    Text("Stats:", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    stats.forEach { stat ->
      Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Text("${stat.stat.name}:", style = MaterialTheme.typography.bodyMedium)
        Text(
          "${stat.baseStat}",
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Bold,
        )
      }
    }
  }
}

@Composable
fun PropertySection(
  title: String,
  value: String,
) {
  Row(
    modifier =
    Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Text(
      text = "$title:",
      style = MaterialTheme.typography.bodyLarge,
      fontWeight = FontWeight.Medium,
    )
    Text(
      text = value,
      style = MaterialTheme.typography.bodyLarge,
      fontWeight = FontWeight.Bold,
    )
  }
}
