package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FilterListOff
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.More
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cache
import data.Type
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import network.Cache


@Composable
fun TopBarFiltering(filterSelected: Boolean) {
    AnimatedVisibility(filterSelected) {
        TopAppBar {
            AnimatedVisibility(FilterState.instance.currentSelectedType.value != Type.NONE) {
                Row(
                    modifier = Modifier.wrapContentWidth().fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.size(8.dp))
                    TypePill(FilterState.instance.currentSelectedType.value)
                }
            }
            AnimatedVisibility(FilterState.instance.currentSelectedGame.value != -1) {
                Row(
                    modifier = Modifier.wrapContentWidth().fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.size(8.dp))
                    Icon(Icons.Default.VideogameAsset, contentDescription = "")
                    Spacer(modifier = Modifier.size(4.dp))
                    if (FilterState.instance.currentSelectedGame.value > 0) {
                        Text(cache.gamesNameList[FilterState.instance.currentSelectedGame.value - 1])
                    }
                }
            }
            AnimatedVisibility(FilterState.instance.currentSelectedNuzlocke.value != null) {
                Row(
                    modifier = Modifier.wrapContentWidth().fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.size(8.dp))
                    if (FilterState.instance.currentSelectedNuzlocke.value != null) {
                        Text("Run: ${FilterState.instance.currentSelectedNuzlocke.value!!.name}")
                    }
                }
            }
            Spacer(Modifier.weight(1f).fillMaxHeight())
            OutlinedButton(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    FilterState.instance.currentSelectedType.value = Type.NONE
                    FilterState.instance.currentSelectedGame.value = -1
                    FilterState.instance.currentSelectedNuzlocke.value = null
                    FilterState.instance.currentSearchString.value = ""
                }) {
                Icon(Icons.Default.FilterListOff, contentDescription = "")
            }
        }
    }
}

@Composable
fun BottomNavigationBar(currentSelected: Int, changeContent: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val HOME = -1
    val SETTINGS = 3
    val PKMNS = 0
    val ITEMS = 4
    val PLACS = 1
    val GAMES = 2

    BottomNavigation {
        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Home, "Home")
        },
            selected = (currentSelected == HOME),
            onClick = {
                changeContent(HOME)
            })

        OutlinedButton(
            onClick = {
                expanded = true
            },
            modifier = Modifier.size(50.dp).padding(4.dp),  //avoid the oval shape
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
        ) {
            Icon(Icons.Default.MoreVert, contentDescription = "")

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    onClick = {
                        changeContent(PKMNS)
                        expanded = false
                    }
                ) {  Icon(imageVector = Icons.Default.List, "Pokémons") }
                DropdownMenuItem(
                    onClick = {
                        changeContent(ITEMS)
                        expanded = false
                    }
                ) {  Icon(imageVector = Icons.Default.Category, "Items") }
                DropdownMenuItem(
                    onClick = {
                        changeContent(PLACS)
                        expanded = false
                    }
                ) {  Icon(imageVector = Icons.Default.Map, "Locations") }
                DropdownMenuItem(
                    onClick = {
                        changeContent(GAMES)
                        expanded = false
                    }
                ) {  Icon(imageVector = Icons.Default.VideogameAsset, "Games") }
            }
        }

        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Settings, "Settings")
        },
            selected = (currentSelected == SETTINGS),
            onClick = {
                changeContent(SETTINGS)
            })
    }
}

@Composable
fun MainScaffold() {
    FilterState.instance.currentSelectedType = remember { mutableStateOf(Type.NONE) }
    FilterState.instance.currentSelectedGame = remember { mutableStateOf(-1) }
    FilterState.instance.currentSelectedNuzlocke = remember { mutableStateOf(null) }
    FilterState.instance.currentSearchString = remember { mutableStateOf("") }

    val filterSelected = FilterState.instance.currentSelectedType.value != Type.NONE ||
            FilterState.instance.currentSelectedGame.value != -1 ||
            FilterState.instance.currentSelectedNuzlocke.value != null

    MaterialTheme(
        colors = if (isSystemInDarkTheme()) DarkColors else LightColors
    ) {
        var content by remember { mutableStateOf(-1) }
        val scope = rememberCoroutineScope()

        scope.launch {
            coroutineScope { cache.preloadTypes() }
            coroutineScope { cache.preloadPokemons() }
            coroutineScope { cache.preloadLocations() }
            coroutineScope { cache.preloadGames() }
            coroutineScope { cache.preloadItems() }
            coroutineScope { cache.loadNuzlockes() }
        }

        Scaffold(
            content = {
                when (content) {
                    0 -> ListAllPokemons(it)
                    1 -> ListAllLocations(it)
                    2 -> ListAllGames(it)
                    3 -> Settings(it)
                    4 -> ListAllItems(it)
                    else -> MainPage(it)
                }
            },
            topBar = { TopBarFiltering(filterSelected) },
            bottomBar = { BottomNavigationBar(content) { content = it } }
        )
    }
}