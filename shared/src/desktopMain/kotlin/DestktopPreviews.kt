import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.Creature
import data.Type
import ui.CreatureCard
import ui.CreatureRowElement
import ui.FilterState
import ui.TypePill

@Preview
@Composable
fun PreviewCreaturesElements() {
    FilterState.instance.currentSelectedType = remember { mutableStateOf(Type.NONE) }
    FilterState.instance.currentSelectedGame = remember { mutableStateOf(-1) }
    FilterState.instance.currentSelectedNuzlocke = remember { mutableStateOf(null) }
    FilterState.instance.currentSearchString = remember { mutableStateOf("") }

    val creatures = listOf(Creature().apply {
        id = 1
        name = "Creatura leggendaria"
        type1 = Type.Ghost
        type2 = Type.Fairy
        isValid = true
        isPreloading = false
        spriteImageUrl = "https://placehold.co/400/png"
        isLegendary = true
    }, Creature().apply {
        id = 2
        name = "Creatura"
        type1 = Type.Fire
        type2 = Type.NONE
        isValid = true
        isPreloading = false
        spriteImageUrl = "https://placehold.co/400/png"
    }, Creature().apply {
        id = 3
        name = "Creaturina"
        type1 = Type.Rock
        type2 = Type.NONE
        isValid = true
        isPreloading = false
        spriteImageUrl = "https://placehold.co/400/png"
        isBaby = true
    }, Creature().apply {
        id = -1
        name = "MissingNo"
        type1 = Type.UNKNOWN
        type2 = Type.NONE
        isValid = false
        isPreloading = true
        spriteImageUrl = "https://placehold.co/400/png"
    })

    Column(modifier = Modifier.padding(16.dp)) {
        LazyColumn {
            items(creatures) {
                CreatureRowElement(it)
            }
        }
        Divider()
        LazyRow {
            items(creatures) {
                CreatureCard(it)
            }
        }
        Divider()
        LazyRow {
            items(Type.values()) {
                TypePill(it)

            }
        }
    }
}