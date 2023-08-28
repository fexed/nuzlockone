package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.Type
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
                                    Text(Cache.instance.gamesList[FilterState.instance.currentSelectedGame.value - 1].title)
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
                        Icon(Icons.Default.FilterList, contentDescription = "")
                        Spacer(modifier = Modifier.size(8.dp))
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
                        Icon(imageVector = Icons.Default.Category, "Items")
                    },
                        selected = (content == 4),
                        onClick = {
                            content = 4
                        })

                    BottomNavigationItem(icon = {
                        Icon(imageVector = Icons.Default.Map, "Locations")
                    },
                        selected = (content == 1),
                        onClick = {
                            content = 1
                        })

                    BottomNavigationItem(icon = {
                        Icon(imageVector = Icons.Default.VideogameAsset, "Games")
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