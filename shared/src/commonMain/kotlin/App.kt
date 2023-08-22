import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import data.Bulbasaur
import data.Charizard
import data.Jigglypuff

expect fun getPlatformName(): String

@Composable
fun UITests() {
    MaterialTheme {
        LazyColumn(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            item { CreatureCard(Bulbasaur, "Saaaur", false) }
            item { CreatureCard(Charizard, "", false) }
            item { CreatureCard(Jigglypuff, "", true) }
        }
    }
}