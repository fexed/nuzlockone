package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.Encounter
import data.Location
import kotlinx.coroutines.launch
import network.Cache
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
                        if (encounter.creature.name == it.creature.name && encounter.typeId == it.typeId) {
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
                Row(modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(8.dp)) {
                    Column {
                        Text(location.regionName, fontSize = 14.sp)
                        Text(location.name, fontSize = 20.sp, modifier = Modifier.padding(0.dp, 8.dp))
                        AnimatedVisibility(areDetailsVisible) {
                            Column {
                                Spacer(modifier = Modifier.padding(8.dp))
                                Text("Encounter list: ", fontSize = 14.sp)
                                LazyRow(modifier = Modifier.wrapContentWidth()) {
                                    items(encounterNumber) {
                                        if (it < encounterList!!.size) {
                                            if (!isFiltered(encounterList!![it])) {
                                                CreatureCard(
                                                    encounterList!![it].creature,
                                                    details = encounterList!![it].typeName
                                                )
                                            }
                                        }
                                    }
                                    item {
                                        if (!areEncountersLoaded) {
                                            Box(
                                                modifier = Modifier.fillMaxHeight().width(150.dp)
                                                    .height(225.dp).padding(8.dp).background(
                                                    shimmerBrush(!areEncountersLoaded)
                                                )
                                            )
                                        }
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

@Composable
fun ListAllLocations(paddingValues: PaddingValues) {
    val scope = rememberCoroutineScope()

    LazyColumn(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, contentPadding = paddingValues) {
        items(count = Cache.instance.numberOfLocations.value) {
            var location by remember { mutableStateOf(Cache.instance.locationsList[it]) }
            var isLoading by remember { mutableStateOf(false) }

            if (location.name == "Loading...") {
                isLoading = true

                location.apply {
                    id = it + 1
                    name = "Loading..."
                    regionName = "Loading..."
                }

                LaunchedEffect(true) {
                    scope.launch {
                        Cache.instance.locationsList[it] = try {
                            PokeApi().getLocationData(it + 1)
                        } catch (e: Exception) {
                            Location().apply {
                                name = e.message ?: "Error"
                            }
                        }
                        location = Cache.instance.locationsList[it]
                        isLoading = false
                    }
                }
            }

            if (location.isValid) LocationRowElement(location, isLoading = isLoading)
        }
    }
}