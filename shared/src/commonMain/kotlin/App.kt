import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.Creature
import data.Game
import data.Location
import data.Type
import kotlinx.coroutines.launch
import network.Cache
import network.PokeApi
import ui.CreatureRowElement
import ui.FilterState
import ui.GameElement
import ui.ListAllGames
import ui.ListAllLocations
import ui.ListAllPokemons
import ui.LocationRowElement
import ui.isFiltered
import ui.shimmerBrush

expect fun getPlatformName(): String
expect val language: String?
expect val country: String?

@Composable
fun mainPage() {
    val cache = Cache.instance

    Column(modifier = Modifier.wrapContentHeight().fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.wrapContentHeight().fillMaxWidth().background(shimmerBrush(cache.numberOfPokemons.value == 1)).padding(8.dp)) {
            Text("There are ${cache.numberOfPokemons.value} creatures in the database",
                modifier = Modifier.padding(16.dp)
            )
        }
        Box(modifier = Modifier.wrapContentHeight().fillMaxWidth().background(shimmerBrush(cache.numberOfLocations.value == 1)).padding(8.dp)) {
            Text("There are ${cache.numberOfLocations.value} locations in the database",
                modifier = Modifier.padding(16.dp)
            )
        }
        Box(modifier = Modifier.wrapContentHeight().fillMaxWidth().background(shimmerBrush(cache.numberOfGames.value == 1)).padding(8.dp)) {
            Text("There are ${cache.numberOfGames.value} games in the database",
                modifier = Modifier.padding(16.dp)
            )
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
                    else -> mainPage()
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