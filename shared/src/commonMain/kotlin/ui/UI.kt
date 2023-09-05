package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterListOff
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cache
import data.Type
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import widthClass

lateinit var loaded: MutableState<Int>
lateinit var toBeLoaded: MutableState<Int>

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
fun BottomNavigationBar(currentSelected: Int, loading: Float = 0.5f, changeContent: (Int) -> Unit) {
    val HOME = -1
    val SETTINGS = 3
    val PKMNS = 0
    val ITEMS = 4
    val PLACS = 1
    val GAMES = 2

    Column {
        if (loading < 1.0f) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), progress = loading)
        }

        if (widthClass() == WindowSizeClass.COMPACT) {
            var expanded by remember { mutableStateOf(false) }
            BottomNavigation {
                AnimatedVisibility(expanded) {
                    Row {
                        BottomNavigationItem(icon = {
                            Icon(imageVector = Icons.Default.List, "Pokémons")
                        },
                            selected = (currentSelected == PKMNS),
                            onClick = {
                                changeContent(PKMNS)
                            })

                        BottomNavigationItem(icon = {
                            Icon(imageVector = Icons.Default.Category, "Items")
                        },
                            selected = (currentSelected == ITEMS),
                            onClick = {
                                changeContent(ITEMS)
                            })

                        BottomNavigationItem(icon = {
                            Icon(imageVector = Icons.Default.Map, "Locations")
                        },
                            selected = (currentSelected == PLACS),
                            onClick = {
                                changeContent(PLACS)
                            })

                        BottomNavigationItem(icon = {
                            Icon(imageVector = Icons.Default.VideogameAsset, "Games")
                        },
                            selected = (currentSelected == GAMES),
                            onClick = {
                                changeContent(GAMES)
                            })

                        BottomNavigationItem(icon = {
                            Icon(Icons.Default.Close, contentDescription = "")
                        },
                            selected = false,
                            onClick = {
                                expanded = false
                            })
                    }
                }

                BottomNavigationItem(icon = {
                    Icon(imageVector = Icons.Default.Home, "Home")
                },
                    selected = (currentSelected == HOME),
                    onClick = {
                        changeContent(HOME)
                    })

                BottomNavigationItem(icon = {
                    Icon(Icons.Default.MoreVert, contentDescription = "")
                },
                    selected = false,
                    onClick = {
                        expanded = true
                    })

                BottomNavigationItem(icon = {
                    Icon(imageVector = Icons.Default.Settings, "Settings")
                },
                    selected = (currentSelected == SETTINGS),
                    onClick = {
                        changeContent(SETTINGS)
                    })
            }
        } else {
            BottomNavigation {
                BottomNavigationItem(icon = {
                    Icon(imageVector = Icons.Default.List, "Pokémons")
                },
                    selected = (currentSelected == PKMNS),
                    onClick = {
                        changeContent(PKMNS)
                    })

                BottomNavigationItem(icon = {
                    Icon(imageVector = Icons.Default.Category, "Items")
                },
                    selected = (currentSelected == ITEMS),
                    onClick = {
                        changeContent(ITEMS)
                    })

                BottomNavigationItem(icon = {
                    Icon(imageVector = Icons.Default.Map, "Locations")
                },
                    selected = (currentSelected == PLACS),
                    onClick = {
                        changeContent(PLACS)
                    })

                BottomNavigationItem(icon = {
                    Icon(imageVector = Icons.Default.VideogameAsset, "Games")
                },
                    selected = (currentSelected == GAMES),
                    onClick = {
                        changeContent(GAMES)
                    })

                BottomNavigationItem(icon = {
                    Icon(imageVector = Icons.Default.Home, "Home")
                },
                    selected = (currentSelected == HOME),
                    onClick = {
                        changeContent(HOME)
                    })

                BottomNavigationItem(icon = {
                    Icon(imageVector = Icons.Default.Settings, "Settings")
                },
                    selected = (currentSelected == SETTINGS),
                    onClick = {
                        changeContent(SETTINGS)
                    })
            }
        }
    }
}

@Composable
fun MainScaffold() {
    loaded = remember { mutableStateOf(0) }
    toBeLoaded = remember { mutableStateOf(6) }
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
            coroutineScope { cache.preloadTypes(); loaded.value++ }
            coroutineScope { cache.preloadPokemons(); loaded.value++ }
            coroutineScope { cache.preloadLocations(); loaded.value++ }
            coroutineScope { cache.preloadGames(); loaded.value++ }
            coroutineScope { cache.preloadItems(); loaded.value++ }
            coroutineScope { cache.loadNuzlockes(); loaded.value++ }
            coroutineScope { cache.loadAllPokemons(); }
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
            bottomBar = { BottomNavigationBar(content, loading = (loaded.value.toFloat()/(toBeLoaded.value))) { content = it } }
        )
    }
}