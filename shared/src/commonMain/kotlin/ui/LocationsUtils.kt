package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.Encounter
import data.Location
import kotlinx.coroutines.launch
import network.PokeApi

@Composable
fun LocationRowElement(location: Location, isLoading: Boolean = false) {
    val scope = rememberCoroutineScope()
    var areDetailsVisible by mutableStateOf(false)
    var areEncountersLoaded by mutableStateOf(false)
    var encounterList : MutableList<Encounter>? = null
    var encounterNumber by remember { mutableStateOf(0) }

    Card(modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(8.dp).clickable {
        if (!areEncountersLoaded) {
            scope.launch {
                encounterList = ArrayList()
                PokeApi().getLocationEncounters(location).collect {
                    var added = false
                    for (encounter in encounterList!!) {
                        if (encounter.creature.name == it.creature.name && encounter.typeId == it.typeId && encounter.chance == it.chance) {
                            added = true
                        }
                    }
                    if (!added) encounterList!!.add(it)
                    encounterNumber = encounterList!!.size
                }
                areEncountersLoaded = true
            }
        }
        areDetailsVisible = !areDetailsVisible
    }) {
        Column(modifier = Modifier.background(
                shimmerBrush(showShimmer = isLoading)
        )) {
            if (!isLoading) {
                Row(modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(4.dp)) {
                    Column {
                        Text("${location.id}", fontSize = 10.sp)
                        Text(location.name, fontSize = 16.sp)
                        Text(location.regionName, fontSize = 14.sp)
                        AnimatedVisibility(areDetailsVisible) {
                            Spacer(modifier = Modifier.padding(8.dp))
                            LazyColumn(modifier = Modifier.height(14.dp * (encounterNumber) + 25.dp)) {
                                item {
                                    Text("Encounter list: ", fontSize = 14.sp)
                                }
                                items(encounterNumber) {
                                    if (it < encounterList!!.size) {
                                        Text(
                                            encounterList!![it].toString(),
                                            modifier = Modifier.padding(4.dp, 0.dp, 0.dp, 4.dp),
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                                item {
                                    if (!areEncountersLoaded) {
                                        Box(modifier = Modifier.width(80.dp).height(25.dp).background(
                                            shimmerBrush(!areEncountersLoaded)
                                        ))
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.height(80.dp))
            }
        }
    }
}