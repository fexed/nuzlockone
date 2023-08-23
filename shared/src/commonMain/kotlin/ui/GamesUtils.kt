package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.Game
import data.Type
import kotlinx.coroutines.launch
import network.Cache
import network.PokeApi

@Composable
fun GameElement(game: Game, isLoading: Boolean = false) {
    val scope = rememberCoroutineScope()
    val currentGame = FilterState.instance.currentSelectedGame
    var currentBitmap: ImageBitmap? by remember { mutableStateOf(null) }

    scope.launch {
        currentBitmap = PokeApi().getImage("https://images.immediate.co.uk/production/volatile/sites/3/2022/04/pokemon-emerald-2759e76.jpg?quality=90&fit=620,413")
    }

    Card(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(8.dp).clickable {
        if (currentGame.value == game.id) {
            currentGame.value = -1
        } else {
            currentGame.value = game.id
        }
    }) {
        Column(modifier = Modifier.fillMaxWidth().wrapContentHeight().background(shimmerBrush(showShimmer = isLoading))) {
            if (!isLoading) {
                currentBitmap?.let { Image(it, contentDescription = "") }
                Text("Pokémon", modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 0.dp))
                Text(game.title, modifier = Modifier.padding(8.dp), fontSize = 20.sp)
            } else {
                Spacer(modifier = Modifier.padding(14.dp))
            }
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