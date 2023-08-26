import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.input
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title
import data.NuzlockRun
import data.Type
import io.ktor.client.HttpClient
import io.ktor.client.plugins.cache.storage.CacheStorage
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import network.Cache
import ui.DarkColors
import ui.DummyNuzlockeElement
import ui.FilterState
import ui.LightColors
import ui.ListAllGames
import ui.ListAllLocations
import ui.ListAllPokemons
import ui.NuzlockeElement
import ui.isFiltered
import ui.shimmerBrush
import kotlin.random.Random

expect fun getPlatformName(): String
expect val language: String?
expect val country: String?
expect fun getCacheFile(): CacheStorage
expect fun getPlatformHttpClient(): HttpClient

@Composable
fun MainPage(paddingValues: PaddingValues) {
    var dialogState = rememberMaterialDialogState()
    val scope = rememberCoroutineScope()
    val cache = Cache.instance
    var newNuzlocke: NuzlockRun? = null

    Column(
        modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn {
            if (cache.numberOfNuzlockes.value == 0) {
                item {
                    DummyNuzlockeElement()
                }
            } else {
                items(cache.numberOfNuzlockes.value) {
                    AnimatedVisibility(!isFiltered(cache.nuzlockes[it])) {
                        NuzlockeElement(cache.nuzlockes[it])
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(onClick = {
                        newNuzlocke =
                            NuzlockRun(nuzlockeId = Random(seed = Clock.System.now().nanosecondsOfSecond).nextInt())
                        newNuzlocke!!.name = "New Nuzlocke"
                        dialogState.show()
                    }, enabled = FilterState.instance.currentSelectedNuzlocke.value == null) {
                        Icon(Icons.Default.Add, contentDescription = "")
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.padding(4.dp))
                Divider()
            }

            item {
                Row(
                    modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(32.dp, 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.List,
                            contentDescription = "",
                            modifier = Modifier.padding(8.dp)
                        )
                        Text("Creatures")
                    }
                    if (cache.numberOfPokemons.value != 1) {
                        Text("${cache.numberOfPokemons.value}")
                    } else {
                        Box(
                            modifier = Modifier.background(shimmerBrush(cache.numberOfPokemons.value == 1))
                                .width(50.dp).height(20.dp)
                        )
                    }
                }
            }

            item {
                Divider()
            }

            item {
                Row(
                    modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(32.dp, 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Place,
                            contentDescription = "",
                            modifier = Modifier.padding(8.dp)
                        )
                        Text("Locations")
                    }
                    if (cache.numberOfLocations.value != 1) {
                        Text("${cache.numberOfLocations.value}")
                    } else {
                        Box(
                            modifier = Modifier.background(shimmerBrush(cache.numberOfLocations.value == 1))
                                .width(50.dp).height(20.dp)
                        )
                    }
                }
            }

            item {
                Divider()
            }

            item {
                Row(
                    modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(32.dp, 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = "",
                            modifier = Modifier.padding(8.dp)
                        )
                        Text("Games")
                    }
                    if (cache.numberOfGames.value != 1) {
                        Text("${cache.numberOfGames.value}")
                    } else {
                        Box(
                            modifier = Modifier.background(shimmerBrush(cache.numberOfGames.value == 1))
                                .width(50.dp).height(20.dp)
                        )
                    }
                }
            }

            item {
                Divider()
            }
        }
    }

    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton("OK") {
                cache.nuzlockes.add(newNuzlocke!!)
                scope.launch {

                }
                cache.numberOfNuzlockes.value += 1
                dialogState.hide()
            }
            negativeButton("Cancel")
        }
    ) {
        title("Add new nuzlocke")
        input(label = "Name", placeholder = newNuzlocke!!.name) {
            newNuzlocke!!.name = it
        }
    }
}

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