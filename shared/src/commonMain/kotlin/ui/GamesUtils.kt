package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.Game

@Composable
fun GameElement(game: Game, isLoading: Boolean = false) {
    Card(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(8.dp)) {
        Column(modifier = Modifier.fillMaxWidth().wrapContentHeight().background(shimmerBrush(showShimmer = isLoading))) {
            if (!isLoading) {
                Text("Pokémon ${game.title}", modifier = Modifier.padding(8.dp))
            } else {
                Spacer(modifier = Modifier.padding(14.dp))
            }
        }
    }
}