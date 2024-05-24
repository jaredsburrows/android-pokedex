@file:OptIn(ExperimentalMaterial3Api::class)

package com.burrowsapps.pokedex.ui.pokemonlist

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.PIXEL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.burrowsapps.pokedex.R
import com.burrowsapps.pokedex.Screen
import com.burrowsapps.pokedex.data.api.model.PokemonEntry
import com.burrowsapps.pokedex.ui.common.PokemonGlideImage
import com.burrowsapps.pokedex.ui.common.TheToolbar
import com.burrowsapps.pokedex.ui.theme.PokedexTheme
import kotlinx.coroutines.flow.Flow
import java.util.Locale

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
private fun PokemonListScreenPreview(navController: NavHostController = rememberNavController()) {
  PokedexTheme {
    PokemonListScreen(navController)
  }
}

@Composable
internal fun PokemonListScreen(
  navController: NavHostController,
  viewModel: PokemonListViewModel = hiltViewModel(),
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

  Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      TheToolbar(
        navController = navController,
        scrollBehavior = scrollBehavior,
        screenName = stringResource(id = R.string.pokemon_list_screen_title),
      )
    },
  ) { paddingValues ->

    TheContent(
      navController = navController,
      innerPadding = paddingValues,
      flow = viewModel.pokemonList,
    )
  }
}

@Composable
private fun TheContent(
  navController: NavHostController,
  innerPadding: PaddingValues,
  flow: Flow<PagingData<PokemonEntry>>,
) {
  val lazyPokemonItems = flow.collectAsLazyPagingItems()

  Column(modifier = Modifier.padding(innerPadding)) {
    when {
      lazyPokemonItems.loadState.refresh is LoadState.Loading && lazyPokemonItems.itemCount == 0 -> {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
          CircularProgressIndicator()
        }
      }

      lazyPokemonItems.loadState.refresh is LoadState.Error && lazyPokemonItems.itemCount == 0 -> {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
          val refreshErrorState = lazyPokemonItems.loadState.refresh as? LoadState.Error
          Text(
            text = "Error: ${refreshErrorState?.error}",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
          )
        }
      }

      lazyPokemonItems.itemCount == 0 -> {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
          Text(
            text = "No Pokémon found!",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
          )
        }
      }

      else -> {
        val listState = rememberLazyListState()

        LazyColumn(
          state = listState,
          modifier =
            Modifier
              .fillMaxSize()
              .semantics { contentDescription = "List" },
        ) {
          items(
            count = lazyPokemonItems.itemCount,
            key = lazyPokemonItems.itemKey { it.name },
          ) { index ->
            val item = lazyPokemonItems[index]!!
            PokemonListItem(navController = navController, pokemonEntry = item)
            HorizontalDivider()
          }
        }
      }
    }
  }
}

@Composable
fun PokemonListItem(
  navController: NavController,
  pokemonEntry: PokemonEntry,
  modifier: Modifier = Modifier,
) {
  // Extract Pokémon number from URL
  val pokemonNumber = pokemonEntry.url.dropLast(1).takeLastWhile { it.isDigit() }

  val imageUrl =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$pokemonNumber.png"

  Column(
    modifier =
      modifier
        .fillMaxWidth()
        .indication(
          indication = rememberRipple(bounded = false),
          interactionSource = remember { MutableInteractionSource() },
        )
        .clickable {
          navController.navigate(Screen.PokemonInfo.route + "/${pokemonEntry.name}")
        }
        .padding(16.dp),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      PokemonGlideImage(imageUrl = imageUrl, size = 50)

      Column(
        modifier =
          Modifier
            .weight(1f)
            .align(Alignment.CenterVertically),
      ) {
        Text(
          text = pokemonEntry.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
          style = MaterialTheme.typography.titleLarge,
        )
      }

      Icon(
        imageVector = Icons.Filled.ChevronRight,
        contentDescription = "Go to details",
        modifier =
          Modifier
            .size(24.dp)
            .align(Alignment.CenterVertically),
      )
    }
  }
}
