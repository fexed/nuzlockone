import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

expect fun getPlatformName(): String
expect val language: String?
expect val country: String?

@Composable
fun ListAllPokemons() {
    val scope = rememberCoroutineScope()
    var number by remember { mutableStateOf(Cache.instance.numberOfPokemons) }

    LaunchedEffect(true) {
        if (Cache.instance.numberOfPokemons == 1) {
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
                Cache.instance.creaturesList = ArrayList(n)
                for (ix in 0 until n) {
                    creature.apply { id = ix + 1 }
                    Cache.instance.creaturesList.add(ix, creature)
                }
                Cache.instance.numberOfPokemons = n
                number = n
            }
        }
    }

    LazyColumn(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        items(count = number) {
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
    var number by remember { mutableStateOf(Cache.instance.numberOfLocations) }

    LaunchedEffect(true) {
        if (Cache.instance.numberOfLocations == 1) {
            scope.launch {
                val n = try {
                    PokeApi().getNumberOfLocations()
                } catch (e: Exception) {
                    0
                }
                val location = Location()
                Cache.instance.locationsList = ArrayList(n)
                for (ix in 0 until n) {
                    location.apply {
                        id = ix + 1
                        isValid = true
                    }
                    Cache.instance.locationsList.add(location)
                }
                Cache.instance.numberOfLocations = n
                number = n
            }
        }
    }

    LazyColumn(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        items(count = number) {
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
    var number by remember { mutableStateOf(Cache.instance.numberOfGames) }

    LaunchedEffect(true) {
        if (Cache.instance.numberOfGames == 1) {
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
                Cache.instance.gamesList = ArrayList(n)
                for (ix in 0 until n) {
                    Cache.instance.gamesList.add(game)
                }
                Cache.instance.numberOfGames = n
                number = n
            }
        }
    }

    LazyColumn(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        items(count = number) {
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
fun MainScaffold() {
    FilterState.instance.currentSelectedType = remember { mutableStateOf(Type.NONE) }
    FilterState.instance.currentSelectedGame = remember { mutableStateOf(-1) }

    MaterialTheme {
        var content by remember { mutableStateOf( 0 ) }

        Scaffold(
            content = {
                when(content) {
                    0 -> ListAllPokemons()
                    1 -> ListAllLocations()
                    2 -> ListAllGames()
                    else -> ListAllPokemons()
                }
            },
            topBar = {
                TopAppBar {
                    Text("Nuzlockone")
                }
            },
            bottomBar = {
                BottomAppBar {
                    Button(onClick = { content = 0 }) {
                        Text("Pokemon")
                    }
                    Button(onClick = { content = 1 }) {
                        Text("Locations")
                    }
                    Button(onClick = { content = 2 }) {
                        Text("Games")
                    }
                }
            }
        )
    }
}