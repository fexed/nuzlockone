package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cache
import com.seiko.imageloader.rememberImagePainter
import data.Game
import kotlinx.coroutines.coroutineScope
import network.PokeApi

@Composable
fun GameElement(game: Game, isLoading: Boolean = false) {
    val scope = rememberCoroutineScope()
    val currentGame = FilterState.instance.currentSelectedGame
    val currentNuzlocke = FilterState.instance.currentSelectedNuzlocke
    var painter = rememberImagePainter(game.imageUrl)

    Card(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(8.dp).clickable {
        if (currentGame.value == game.id) {
            currentGame.value = -1
        } else {
            currentGame.value = game.id
        }
        if (currentNuzlocke.value != null) {
            currentNuzlocke.value!!.gameId = currentGame.value
        }
    }) {
        Column(
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
                .background(shimmerBrush(showShimmer = isLoading))
        ) {
            if (!isLoading) {
                Box(contentAlignment = Alignment.BottomStart) {
                    Image(
                        painter = painter,
                        contentDescription = "",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.FillWidth
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth().wrapContentHeight().background(
                            (if (isSystemInDarkTheme()) Color.Black else Color.White).copy(alpha = 0.5f)
                        )
                    ) {
                        Text("Pokémon", modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 0.dp))
                        Text(game.title, modifier = Modifier.padding(4.dp), fontSize = 20.sp)
                        if (currentGame.value == game.id) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text("Selected", fontSize = 10.sp, color = Color.Green)
                            }
                        }
                    }
                }
            } else {
                Spacer(modifier = Modifier.padding(14.dp))
            }
        }
    }
}

@Composable
fun ListAllGames(paddingValues: PaddingValues) {
    LazyColumn(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = paddingValues
    ) {
        items(count = cache.numberOfGames.value) {
            var game by remember { mutableStateOf(cache.gamesList[it]) }
            var isLoading by remember { mutableStateOf(game.title == "Loading...") }

            if (isLoading) {
                LaunchedEffect(true) {
                    coroutineScope {
                        cache.gamesList[it] = try {
                            PokeApi().getGameData(it + 1)
                        } catch (e: Exception) {
                            Game().apply {
                                title = e.message ?: "Error"
                            }
                        }
                        game = cache.gamesList[it]
                        game.isValid = true
                        isLoading = false
                    }
                }
            }

            if (game.isValid && !isFiltered(game)) GameElement(game, isLoading)
        }
    }
}