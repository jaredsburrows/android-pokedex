@file:OptIn(ExperimentalMaterial3Api::class)

package com.burrowsapps.pokedex.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.burrowsapps.pokedex.R
import com.burrowsapps.pokedex.Screen

@Composable
fun TheToolbar(
  navController: NavHostController,
  scrollBehavior: TopAppBarScrollBehavior,
  screenName: String,
) {
  TheToolBar(
    navController = navController,
    scrollBehavior = scrollBehavior,
    screenName = screenName,
  )
}

@Composable
private fun TheToolBar(
  navController: NavHostController,
  scrollBehavior: TopAppBarScrollBehavior,
  screenName: String,
) {
  var showMenu by remember { mutableStateOf(false) }
  val moreTooltipState = remember { TooltipState() }

  TopAppBar(
    title = { Text(text = screenName) },
    navigationIcon = {
      if (navController.previousBackStackEntry != null) {
        IconButton(onClick = { navController.popBackStack() }) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.menu_back),
          )
        }
      }
    },
    actions = {
      TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = { Text("Show menu") },
        state = moreTooltipState,
      ) {
        IconButton(onClick = { showMenu = !showMenu }) {
          Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.menu_more),
          )
        }
      }
      DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { showMenu = false },
      ) {
        DropdownMenuItem(
          onClick = {
            navController.navigate(Screen.LicenseInfo.route)
            showMenu = false
          },
          text = { Text(text = stringResource(R.string.license_screen_title)) },
        )
      }
    },
    scrollBehavior = scrollBehavior,
  )
}
