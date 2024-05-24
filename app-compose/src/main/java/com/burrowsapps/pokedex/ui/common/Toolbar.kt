@file:OptIn(ExperimentalMaterial3Api::class)

package com.burrowsapps.pokedex.ui.common

import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
  val showMenu = remember { mutableStateOf(false) }

  TheToolBar(
    navController = navController,
    scrollBehavior = scrollBehavior,
    showMenu = showMenu,
    screenName = screenName,
  )
}

@Composable
private fun TheToolBar(
  navController: NavHostController,
  scrollBehavior: TopAppBarScrollBehavior,
  showMenu: MutableState<Boolean>,
  screenName: String,
) {
  val moreTooltipState = remember { TooltipState() }

  TopAppBar(
    title = {
      Text(
        text = screenName,
      )
    },
    actions = {
      // Overflow menu item
      TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = { Text("Show menu") },
        state = moreTooltipState,
      ) {
        IconButton(
          onClick = { showMenu.value = !showMenu.value },
        ) {
          Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.menu_more),
          )
        }
      }
      // Overflow menu
      DropdownMenu(
        expanded = showMenu.value,
        onDismissRequest = { showMenu.value = false },
      ) {
        DropdownMenuItem(
          onClick = {
            navController.navigate(Screen.LicenseInfo.route)
            showMenu.value = false
          },
          text = { Text(text = stringResource(R.string.license_screen_title)) },
        )
      }
    },
    scrollBehavior = scrollBehavior,
  )
}
