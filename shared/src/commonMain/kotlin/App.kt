import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import data.Bulbasaur
import data.Charizard
import data.Jigglypuff
import ui.CreatureCard
import ui.CreatureRowElement

expect fun getPlatformName(): String

@Composable
fun UITests() {
    MaterialTheme {
        LazyColumn(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            item { CreatureRowElement(Bulbasaur, "Saaaur", false) }
            item { CreatureRowElement(Charizard, "", false) }
            item { CreatureRowElement(Jigglypuff, "", true) }
            item { CreatureCard(Bulbasaur, "", true) }
            item { CreatureCard(Charizard, "", true) }
            item { CreatureCard(Jigglypuff, "", false) }
        }
    }
}