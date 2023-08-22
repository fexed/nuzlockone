import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
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
import data.Location
import data.Type
import data.creaturesList
import data.locationsList
import kotlinx.coroutines.launch
import network.PokeApi
import ui.CreatureRowElement
import ui.LocationRowElement

expect fun getPlatformName(): String
expect val language: String?
expect val country: String?

@Composable
fun ListAllPokemons() {
    val scope = rememberCoroutineScope()
    var number by remember { mutableStateOf(0) }

    LaunchedEffect(true) {
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
            }
            creaturesList = ArrayList(n)
            for (ix in 0..n) {
                creature.apply { id = ix + 1 }
                creaturesList.add(creature)
            }
            number = n
        }
    }

    LazyColumn(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        items(count = number) {
            var creature by remember { mutableStateOf(creaturesList[it]) }

            if (creature.name == "Loading...") {
                creature.apply {
                    id = it + 1
                    name = "Loading..."
                    type1 = Type.NONE
                    type2 = Type.NONE
                }

                LaunchedEffect(true) {
                    scope.launch {
                        creaturesList[it] = try {
                            PokeApi().getCreatureData(it + 1)
                        } catch (e: Exception) {
                            Creature().apply {
                                id = -1
                                name = e.message ?: "Error"
                                type1 = Type.NONE
                                type2 = Type.NONE
                            }
                        }
                        creature = creaturesList[it]
                    }
                }
            }

            if (creature.isValid) CreatureRowElement(creature)
        }
    }
}

@Composable
fun ListAllLocations() {
    val scope = rememberCoroutineScope()
    var number by remember { mutableStateOf(0) }

    LaunchedEffect(true) {
        scope.launch {
            val n = try {
                PokeApi().getNumberOfLocations()
            } catch (e: Exception) {
                0
            }
            val location = Location()
            locationsList = ArrayList(n)
            for (ix in 0..n) {
                location.apply { id = ix + 1 }
                locationsList.add(location)
            }
            number = n
        }
    }

    LazyColumn(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        items(count = number) {
            var location by remember { mutableStateOf(locationsList[it]) }

            if (location.name == "Loading...") {
                location.apply {
                    id = it + 1
                    name = "Loading..."
                    regionName = "Loading..."
                }

                LaunchedEffect(true) {
                    scope.launch {
                        locationsList[it] = try {
                            PokeApi().getLocationData(it + 1)
                        } catch (e: Exception) {
                            Location().apply {
                                name = e.message ?: "Error"
                            }
                        }
                        location = locationsList[it]
                    }
                }
            }

            if (location.isValid) LocationRowElement(location)
        }
    }
}

@Composable
fun Tests() {
    Column {
        ListAllLocations()
    }
}

@Composable
fun MainScaffold(content: @Composable (PaddingValues) -> Unit) {
    MaterialTheme {
        Scaffold(
            content = content,
            topBar = {
                TopAppBar {
                    Text("Nuzlockone")
                }
            }
        )
    }
}