package ui

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import data.Type
import data.getTypeName
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import network.Cache

@Composable
fun MainScaffold() {
    val cache = Cache.instance
    FilterState.instance.currentSelectedType = remember { mutableStateOf(Type.NONE) }
    FilterState.instance.currentSelectedGame = remember { mutableStateOf(-1) }
    FilterState.instance.currentSelectedNuzlocke = remember { mutableStateOf(null) }

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
            topBar = {
                 AnimatedVisibility(filterSelected) {
                     TopAppBar {
                         Icon(Icons.Default.FilterList, contentDescription = "")
                         AnimatedVisibility(FilterState.instance.currentSelectedType.value != Type.NONE) {
                             TypePill(FilterState.instance.currentSelectedType.value)
                         }
                         AnimatedVisibility(FilterState.instance.currentSelectedGame.value != -1) {
                             Text("Game: ${Cache.instance.gamesList[FilterState.instance.currentSelectedGame.value].title}")
                         }
                         AnimatedVisibility(FilterState.instance.currentSelectedNuzlocke.value != null) {
                             Text("Run: ${FilterState.instance.currentSelectedNuzlocke.value!!.name}")
                         }
                     }
                 }
            },
            bottomBar = {
                BottomNavigation {
                    BottomNavigationItem(icon = {
                        Icon(imageVector = Icons.Default.Home, "Home")
                    },
                        selected = (content == -1),
                        onClick = {
                            content = -1
                        })

                    BottomNavigationItem(icon = {
                        Icon(imageVector = Icons.Default.List, "Pokémons")
                    },
                        selected = (content == 0),
                        onClick = {
                            content = 0
                        })

                    BottomNavigationItem(icon = {
                        Icon(imageVector = Icons.Default.Search, "Items")
                    },
                        selected = (content == 4),
                        onClick = {
                            content = 4
                        })

                    BottomNavigationItem(icon = {
                        Icon(imageVector = Icons.Default.Place, "Locations")
                    },
                        selected = (content == 1),
                        onClick = {
                            content = 1
                        })

                    BottomNavigationItem(icon = {
                        Icon(imageVector = Icons.Default.PlayArrow, "Games")
                    },
                        selected = (content == 2),
                        onClick = {
                            content = 2
                        })

                    BottomNavigationItem(icon = {
                        Icon(imageVector = Icons.Default.Settings, "Settings")
                    },
                        selected = (content == 3),
                        onClick = {
                            content = 3
                        })
                }
            }
        )
    }
}