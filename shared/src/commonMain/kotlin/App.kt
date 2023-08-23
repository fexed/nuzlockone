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
import ui.LocationRowElement
import ui.isFiltered
import ui.shimmerBrush

expect fun getPlatformName(): String
expect val language: String?
expect val country: String?

@Composable
fun ListAllPokemons() {
    val scope = rememberCoroutineScope()

    LazyColumn(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        items(count = Cache.instance.numberOfPokemons.value) {
            var creature by remember { mutableStateOf(Cache.instance.creaturesList[it]) }
            var isLoading by remember { mutableStateOf(false) }

            if (creature.name == "Loading...") {
                isLoading = true
                creature.apply {
                    id = it + 1
                    name = "Loading..."
                    type1 = Type.NONE
                    type2 = Type.NONE
                }

                LaunchedEffect(true) {
                    scope.launch {
                        Cache.instance.creaturesList[it] = try {
                            PokeApi().getCreatureData(it + 1)
                        } catch (e: Exception) {
                            Creature().apply {
                                id = -1
                                name = e.message ?: "Error"
                                type1 = Type.NONE
                                type2 = Type.NONE
                            }
                        }
                        creature = Cache.instance.creaturesList[it]
                        isLoading = false
                    }
                }
            }

            if (creature.isValid) {
                if (isLoading || !isLoading && !isFiltered(creature)) {
                    CreatureRowElement(creature, isLoading = isLoading)
                }
            }
        }
    }
}

@Composable
fun ListAllLocations() {
    val scope = rememberCoroutineScope()

    LazyColumn(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        items(count = Cache.instance.numberOfLocations.value) {
            var location by remember { mutableStateOf(Cache.instance.locationsList[it]) }
            var isLoading by remember { mutableStateOf(false) }

            if (location.name == "Loading...") {
                isLoading = true

                location.apply {
                    id = it + 1
                    name = "Loading..."
                    regionName = "Loading..."
                }

                LaunchedEffect(true) {
                    scope.launch {
                        Cache.instance.locationsList[it] = try {
                            PokeApi().getLocationData(it + 1)
                        } catch (e: Exception) {
                            Location().apply {
                                name = e.message ?: "Error"
                            }
                        }
                        location = Cache.instance.locationsList[it]
                        isLoading = false
                    }
                }
            }

            if (location.isValid) LocationRowElement(location, isLoading = isLoading)
        }
    }
}

@Composable
fun ListAllGames() {
    val scope = rememberCoroutineScope()

    LazyColumn(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        items(count = Cache.instance.numberOfGames.value) {
            var game by remember { mutableStateOf(Cache.instance.gamesList[it]) }
            var isLoading by remember { mutableStateOf(false) }

            if (game.title == "Loading...") {
                isLoading = true

                LaunchedEffect(true) {
                    scope.launch {
                        Cache.instance.gamesList[it] = try {
                            PokeApi().getGameData(it + 1)
                        } catch (e: Exception) {
                            Game().apply {
                                title = e.message ?: "Error"
                            }
                        }
                        game.isValid = true
                        game = Cache.instance.gamesList[it]
                        isLoading = false
                    }
                }
            }

            if (game.isValid) GameElement(game, isLoading)
        }
    }
}

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

        scope.launch {
            val n = try {
                PokeApi().getNumberOfGames()
            } catch (e: Exception) {
                0
            }
            val game = Game().apply {
                title = "Loading..."
                isValid = true
            }
            cache.gamesList = ArrayList(n)
            for (ix in 0 until n) {
                cache.gamesList.add(game)
            }
            cache.numberOfGames.value = n
        }

        scope.launch {
            scope.launch {
                val n = try {
                    PokeApi().getNumberOfLocations()
                } catch (e: Exception) {
                    0
                }
                val location = Location()
                cache.locationsList = ArrayList(n)
                for (ix in 0 until n) {
                    location.apply {
                        id = ix + 1
                        isValid = true
                    }
                    cache.locationsList.add(location)
                }
                cache.numberOfLocations.value = n
            }
        }

        scope.launch {
            val n = try {
                PokeApi().getNumberOfPokemons()
            } catch (e: Exception) {
                0
            }
            val creature = Creature().apply {
                id = -1
                name = "Loading..."
                type1 = Type.NONE
                type2 = Type.NONE
                isValid = true
            }
            cache.creaturesList = ArrayList(n)
            for (ix in 0 until n) {
                creature.apply { id = ix + 1 }
                cache.creaturesList.add(ix, creature)
            }
            cache.numberOfPokemons.value = n
        }

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