package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.input
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title
import data.NuzlockRun
import kotlinx.datetime.Clock
import network.Cache
import kotlin.random.Random

@Composable
fun MainPage(paddingValues: PaddingValues) {
    var dialogState = rememberMaterialDialogState()
    val cache = Cache.instance
    var newNuzlocke: NuzlockRun? = null

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
                        newNuzlocke!!.name = "New Nuzlocke"
                        dialogState.show()
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

    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton("OK") {
                cache.nuzlockes.add(newNuzlocke!!)
                cache.numberOfNuzlockes.value += 1
                dialogState.hide()
            }
            negativeButton("Cancel")
        }
    ) {
        title("Add new nuzlocke")
        input(label = "Name", placeholder = newNuzlocke!!.name) {
            newNuzlocke!!.name = it
        }
    }
}