package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import data.NuzlockRun
import kotlinx.datetime.Clock
import network.Cache
import kotlin.random.Random

@Composable
fun MainPage(paddingValues: PaddingValues) {
    var showDialog by remember { mutableStateOf(false) }
    val cache = Cache.instance
    var newNuzlocke = NuzlockRun(nuzlockeId = Random(seed = Clock.System.now().nanosecondsOfSecond).nextInt())

    Column(
        modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn {
            if (cache.numberOfNuzlockes.value == 0) {
                item {
                    DummyNuzlockeElement()
                }
            } else {
                items(cache.numberOfNuzlockes.value) {
                    AnimatedVisibility(!isFiltered(cache.nuzlockes[it])) {
                        NuzlockeElement(cache.nuzlockes[it])
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(onClick = {
                        newNuzlocke =
                            NuzlockRun(nuzlockeId = Random(seed = Clock.System.now().nanosecondsOfSecond).nextInt())
                        newNuzlocke.name = "New Nuzlocke"
                        showDialog = true
                    }, enabled = FilterState.instance.currentSelectedNuzlocke.value == null) {
                        Icon(Icons.Default.Add, contentDescription = "")
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.padding(4.dp))
            }

            item {
                StatsCard()
            }

        }
    }

    if (showDialog) {
         var newName by remember { mutableStateOf(newNuzlocke.name) }
        Dialog(
            onDismissRequest = { showDialog = false },
        ) {
            Card {
                Column(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Add a new nuzlocke run")
                    Spacer(modifier = Modifier.size(8.dp))
                    TextField(
                        value = newName,
                        onValueChange = {
                            newName = it
                        })
                    Spacer(modifier = Modifier.size(16.dp))
                    Button(onClick = {
                        newNuzlocke.name = newName
                        cache.nuzlockes.add(newNuzlocke)
                        cache.numberOfNuzlockes.value += 1
                        showDialog = false
                    }) {
                        Text("OK")
                    }
                }
            }
        }
    }
}