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
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cache
import data.NuzlockRun


@Composable
fun DummyNuzlockeElement() {
    Card(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(8.dp)) {
        Column {
            Row(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.wrapContentHeight()) {
                    Text("No Nuzlockes saved")
                    Text("Use the button below to add one", fontSize = 10.sp)
                }
            }
        }
    }
}
@Composable
fun NuzlockeElement(nuzlockRun: NuzlockRun) {
    val currentNuzlocke = FilterState.instance.currentSelectedNuzlocke
    var isDeletePressed by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(8.dp).clickable {
        if (currentNuzlocke.value?.nuzlockeId == nuzlockRun.nuzlockeId) {
            isDeletePressed = false
            currentNuzlocke.value = null
            FilterState.instance.currentSelectedGame.value = -1
        } else {
            currentNuzlocke.value = nuzlockRun
            FilterState.instance.currentSelectedGame.value = nuzlockRun.gameId
        }
    }) {
        Column {
            Row(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.wrapContentHeight()) {
                    Text(nuzlockRun.name)
                    AnimatedVisibility(currentNuzlocke.value?.nuzlockeId != nuzlockRun.nuzlockeId) {
                        if (nuzlockRun.gameId > 0) {
                            Text(
                                "Pokémon " + cache.gamesNameList[nuzlockRun.gameId - 1],
                                fontSize = 10.sp
                            )
                        } else {
                            Text("No game selected", fontSize = 10.sp)
                        }
                    }
                }
                AnimatedVisibility(currentNuzlocke.value?.nuzlockeId == nuzlockRun.nuzlockeId) {
                    AnimatedVisibility(isDeletePressed) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            OutlinedButton(onClick = {
                                isDeletePressed = false
                            }) {
                                Icon(Icons.Default.Close, contentDescription = "")
                            }
                            Spacer(modifier = Modifier.padding(8.dp))
                            Button(onClick = {
                                cache.nuzlockes.remove(nuzlockRun)
                                if (currentNuzlocke.value?.nuzlockeId == nuzlockRun.nuzlockeId) {
                                    currentNuzlocke.value = null
                                }
                                cache.numberOfNuzlockes.value -= 1
                            }) {
                                Text("Confirm?")
                            }
                        }
                    }

                    AnimatedVisibility(!isDeletePressed) {
                        OutlinedButton(onClick = {
                            isDeletePressed = true
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "")
                        }
                    }
                }
            }
            AnimatedVisibility(currentNuzlocke.value?.nuzlockeId == nuzlockRun.nuzlockeId) {
                Column {
                    Row(modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                            if (currentNuzlocke.value != null && currentNuzlocke.value!!.gameId > 0) {
                                Text("Pokémon " + cache.gamesNameList[currentNuzlocke.value!!.gameId - 1])
                                Text("Game")
                            } else {
                                Text("No game selected")
                            }
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