package ui

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
            coroutineScope { cache.loadNuzlockes() }
        }

        Scaffold(
            content = {
                when (content) {
                    0 -> ListAllPokemons(it)
                    1 -> ListAllLocations(it)
                    2 -> ListAllGames(it)
                    else -> MainPage(it)
                }
            },
//            topBar = {
//                TopAppBar(title = { Text("Nuzlockone") })
//            },
            bottomBar = {
                BottomNavigation {
                    BottomNavigationItem(icon = {
                        Icon(imageVector = Icons.Default.Home, "")
                    },
                        label = { Text(text = "Home") },
                        selected = (content == -1),
                        onClick = {
                            content = -1
                        })

                    BottomNavigationItem(icon = {
                        Icon(imageVector = Icons.Default.List, "")
                    },
                        label = { Text(text = "Pokemon") },
                        selected = (content == 0),
                        onClick = {
                            content = 0
                        })

                    BottomNavigationItem(icon = {
                        Icon(imageVector = Icons.Default.Place, "")
                    },
                        label = { Text(text = "Locations") },
                        selected = (content == 1),
                        onClick = {
                            content = 1
                        })

                    BottomNavigationItem(icon = {
                        Icon(imageVector = Icons.Default.PlayArrow, "")
                    },
                        label = { Text(text = "Games") },
                        selected = (content == 2),
                        onClick = {
                            content = 2
                        })
                }
            }
        )
    }
}