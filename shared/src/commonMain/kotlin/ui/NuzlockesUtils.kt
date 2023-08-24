package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.NuzlockRun
import network.Cache

@Composable
fun NuzlockeElement(nuzlockRun: NuzlockRun) {
    val currentNuzlocke = FilterState.instance.currentSelectedNuzlocke

    Card(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(8.dp).clickable {
        if (currentNuzlocke.value?.nuzlockeId == nuzlockRun.nuzlockeId) {
            currentNuzlocke.value = null
        } else {
            currentNuzlocke.value = nuzlockRun
        }
        println("LOGFX ${currentNuzlocke.value?.nuzlockeId}")
    }) {
        Column {
            Row(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.wrapContentHeight()) {
                    Text("Nuzlocke ${nuzlockRun.nuzlockeId}")
                }
                AnimatedVisibility(currentNuzlocke.value?.nuzlockeId == nuzlockRun.nuzlockeId) {
                    Button(onClick = {
                        Cache.instance.nuzlockes.remove(nuzlockRun)
                        if (currentNuzlocke.value?.nuzlockeId == nuzlockRun.nuzlockeId) {
                            currentNuzlocke.value = null
                        }
                        Cache.instance.numberOfNuzlockes.value -= 1
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "")
                    }
                }
            }
            AnimatedVisibility(currentNuzlocke.value?.nuzlockeId == nuzlockRun.nuzlockeId) {
                Column {
                    Text("Currently selected", modifier = Modifier.padding(8.dp, 0.dp))
                    Row(modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceAround) {
                        Column(modifier = Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${nuzlockRun.gameId}")
                            Text("Game")
                        }
                        Column(modifier = Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${nuzlockRun.speciesCaughtIds.size}")
                            Text("Caught species")
                        }
                    }
                }
            }
        }
    }
}