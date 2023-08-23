import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
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
import network.Cache
import ui.FilterState
import ui.ListAllGames
import ui.ListAllLocations
import ui.ListAllPokemons
import ui.shimmerBrush

expect fun getPlatformName(): String
expect val language: String?
expect val country: String?

@Composable
fun StatisticsPage() {
    val cache = Cache.instance

    Column(modifier = Modifier.wrapContentHeight().fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(8.dp, 8.dp), horizontalArrangement = Arrangement.SpaceAround) {
            Text("There are")
            if (cache.numberOfPokemons.value != 1) {
                Text("${cache.numberOfPokemons.value}")
            } else {
                Box(modifier = Modifier.background(shimmerBrush(cache.numberOfPokemons.value == 1)).width(50.dp).height(20.dp))
            }
            Text("creatures in the database")
        }
        Row(modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(8.dp, 4.dp), horizontalArrangement = Arrangement.SpaceAround) {
            Text("There are")
            if (cache.numberOfLocations.value != 1) {
                Text("${cache.numberOfLocations.value}")
            } else {
                Box(modifier = Modifier.background(shimmerBrush(cache.numberOfLocations.value == 1)).width(50.dp).height(20.dp))
            }
            Text("locations in the database")
        }
        Row(modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(8.dp, 8.dp), horizontalArrangement = Arrangement.SpaceAround) {
            Text("There are")
            if (cache.numberOfGames.value != 1) {
                Text("${cache.numberOfGames.value}")
            } else {
                Box(modifier = Modifier.background(shimmerBrush(cache.numberOfGames.value == 1)).width(50.dp).height(20.dp))
            }
            Text("games in the database")
        }
    }
}

@Composable
fun MainScaffold() {
    val cache = Cache.instance
    FilterState.instance.currentSelectedType = remember { mutableStateOf(Type.NONE) }
    FilterState.instance.currentSelectedGame = remember { mutableStateOf(-1) }

    MaterialTheme {
        var content by remember { mutableStateOf( -1 ) }
        val scope = rememberCoroutineScope()

        cache.preloadPokemons(scope)
        cache.preloadLocations(scope)
        cache.preloadGames(scope)

        Scaffold(
            content = {
                when(content) {
                    0 -> ListAllPokemons()
                    1 -> ListAllLocations()
                    2 -> ListAllGames()
                    else -> StatisticsPage()
                }
            },
            topBar = {
                     TopAppBar(title = { Text("Nuzlockone") })
            },
            bottomBar = {
                BottomNavigation {
                    BottomNavigationItem(icon = {
                        Icon(imageVector = Icons.Default.Home, "")
                    },
                        label = { Text(text = "Stats") },
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
                        Icon(imageVector = Icons.Default.List, "")
                    },
                        label = { Text(text = "Locations") },
                        selected = (content == 1),
                        onClick = {
                            content = 1
                        })

                    BottomNavigationItem(icon = {
                        Icon(imageVector = Icons.Default.List, "")
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