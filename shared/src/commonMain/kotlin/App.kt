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
import data.Bulbasaur
import data.Charizard
import data.Creature
import data.Jigglypuff
import data.Type
import kotlinx.coroutines.launch
import network.PokeApi
import ui.CreatureCard
import ui.CreatureRowElement

expect fun getPlatformName(): String

@Composable
fun UITests() {
    LazyColumn(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        item { CreatureRowElement(Bulbasaur, "Saaaur", false) }
        item { CreatureRowElement(Charizard, "", false) }
        item { CreatureRowElement(Jigglypuff, "", true) }
        item { CreatureCard(Bulbasaur, "", true) }
        item { CreatureCard(Charizard, "", true) }
        item { CreatureCard(Jigglypuff, "", false) }
    }
}

@Composable
fun NetworkTests() {
    val scope = rememberCoroutineScope()
    var number by remember { mutableStateOf(0) }

    LaunchedEffect(true) {
        scope.launch {
            number = try {
                PokeApi().getNumberOfPokemons()
            } catch (e: Exception) {
                0
            }
        }
    }

    LazyColumn(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        items(count = number) {
            var creature by remember { mutableStateOf(Creature().apply { id = it + 1; name = "Loading..."; type1 = Type.NONE; type2 = Type.NONE }) }
            LaunchedEffect(true) {
                scope.launch {
                    creature = try {
                        PokeApi().getCreatureData(it + 1)
                    } catch (e: Exception) {
                        Creature().apply {
                            name = e.message ?: "Error"
                            type1 = Type.NONE
                            type2 = Type.NONE
                        }
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