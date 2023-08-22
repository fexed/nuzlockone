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
import data.Type
import data.creaturesList
import kotlinx.coroutines.launch
import network.PokeApi
import ui.CreatureCard
import ui.CreatureRowElement

expect fun getPlatformName(): String
expect val language: String?
expect val country: String?

@Composable
fun NetworkTests() {
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

            CreatureRowElement(creature)
        }
    }
}

@Composable
fun Tests() {
    Column {
        NetworkTests()
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