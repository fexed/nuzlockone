package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cache
import com.seiko.imageloader.rememberImagePainter
import data.Creature
import data.Type
import data.getTypeColor
import data.getTypeName
import kotlinx.coroutines.coroutineScope
import network.PokeApi


@Composable
fun CreatureRowElement(creature: Creature, isLoading: Boolean = false) {
    var areDetailsVisible by mutableStateOf(false)
    val currentNuzlocke = FilterState.instance.currentSelectedNuzlocke
    var caught by remember {
        mutableStateOf(
            if (currentNuzlocke.value != null) {
                currentNuzlocke.value!!.speciesCaughtIds.contains(creature.id)
            } else false
        )
    }
    val borderColor = if (caught) Color.Green else Color.Transparent
    var currentDescription by mutableStateOf(0)
    val painter = rememberImagePainter(creature.spriteImageUrl)
    var showDialog by remember { mutableStateOf(false) }

    Card(modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(8.dp).clickable {
        if (creature.flavorTexts.isNotEmpty()) areDetailsVisible = !areDetailsVisible
    }) {
        Column(
            modifier = Modifier.background(
                shimmerBrush(showShimmer = isLoading)
            )
        ) {
            if (!isLoading) {
                Row(
                    modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter,
                        null,
                        modifier = Modifier.size(75.dp)
                            .clip(CircleShape)
                            .border(2.dp, borderColor, CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        getTypeColor(creature.type1).copy(alpha = 0.75f),
                                        getTypeColor(
                                            if (creature.type2 != Type.NONE)
                                                creature.type2
                                            else
                                                creature.type1
                                        ).copy(alpha = 0.25f)
                                    ), radius = 125f
                                )
                            ).clickable {
                                showDialog = true
                            }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("${creature.id}", fontSize = 10.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(creature.name, fontSize = 16.sp)
                            if (creature.isLegendary || creature.isMithycal) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.Star, contentDescription = "")
                            }
                        }
                        Row {
                            TypePill(creature.type1)
                            Spacer(modifier = Modifier.width(8.dp))
                            TypePill(creature.type2)
                        }
                    }
                    if (currentNuzlocke.value != null) {
                        Spacer(
                            Modifier.weight(1f).fillMaxHeight()
                        )
                        RadioButton(selected = caught, onClick = {
                            if (!caught) {
                                currentNuzlocke.value!!.speciesCaughtIds.add(creature.id)
                            } else {
                                currentNuzlocke.value!!.speciesCaughtIds.remove(creature.id)
                            }
                            caught = !caught
                        })
                    }
                }
                AnimatedVisibility(areDetailsVisible) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                modifier = Modifier.padding(8.dp),
                                onClick = {
                                    currentDescription -= 1
                                },
                                enabled = currentDescription > 0
                            ) { Icon(Icons.Default.ArrowBack, contentDescription = "") }
                            Text("Flavor texts")
                            Button(
                                modifier = Modifier.padding(8.dp),
                                onClick = {
                                    currentDescription += 1
                                },
                                enabled = currentDescription < creature.flavorTexts.size - 1
                            ) { Icon(Icons.Default.ArrowForward, contentDescription = "") }
                        }
                        Text(
                            creature.flavorTexts[currentDescription].text,
                            modifier = Modifier.padding(16.dp, 8.dp).fillMaxWidth()
                                .wrapContentHeight()
                        )
                    }
                }
            } else {
                Box(modifier = Modifier.height(80.dp))
            }
        }
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
        ) {
            Card {
                Column(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row {
                        Image(
                            rememberImagePainter(creature.spriteImageUrl),
                            null,
                            modifier = Modifier.size(125.dp)
                        )

                        Image(
                            rememberImagePainter(creature.shinySpriteImageUrl),
                            null,
                            modifier = Modifier.size(125.dp)
                        )
                    }

                    Row {
                        Image(
                            rememberImagePainter(creature.backSpriteImageUrl),
                            null,
                            modifier = Modifier.size(125.dp)
                        )

                        Image(
                            rememberImagePainter(creature.backShinySpriteImageUrl),
                            null,
                            modifier = Modifier.size(125.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CreatureCard(creature: Creature, details: String = "") {
    val currentNuzlocke = FilterState.instance.currentSelectedNuzlocke
    var caught by remember {
        mutableStateOf(
            if (currentNuzlocke.value != null) {
                currentNuzlocke.value!!.speciesCaughtIds.contains(creature.id)
            } else false
        )
    }
    val borderColor = if (caught) Color.Green else Color.Transparent
    val painter = rememberImagePainter(creature.spriteImageUrl)

    Card(modifier = Modifier.wrapContentHeight().width(150.dp).padding(8.dp)) {
        Column(
            modifier = Modifier.wrapContentHeight().wrapContentWidth().padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("${creature.id}", fontSize = 10.sp)
            Text(creature.name, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter,
                null,
                modifier = Modifier.size(75.dp)
                    .clip(CircleShape)
                    .border(2.dp, borderColor, CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                getTypeColor(creature.type1), getTypeColor(
                                    if (creature.type2 != Type.NONE) creature.type2 else creature.type1
                                )
                            ), radius = 125f
                        )
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
            TypePill(creature.type1)
            Spacer(modifier = Modifier.width(4.dp))
            TypePill(creature.type2)
            Text(details, fontSize = 12.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun TypePill(type: Type) {
    val color = getTypeColor(type)

    if (type != Type.NONE) {
        OutlinedButton(
            onClick = {
                val currentType = FilterState.instance.currentSelectedType
                if (currentType.value == type) {
                    currentType.value = Type.NONE
                } else {
                    currentType.value = type
                }
            },
            border = BorderStroke(1.dp, color),
            shape = RoundedCornerShape(50),
            colors = if (FilterState.instance.currentSelectedType.value != type) ButtonDefaults.outlinedButtonColors(
                contentColor = color
            ) else ButtonDefaults.outlinedButtonColors(contentColor = Color.White, backgroundColor = color)
        ) { Text(getTypeName(type)) }
    }
}

@Composable
fun ListAllPokemons(paddingValues: PaddingValues) {
    LazyColumn(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = paddingValues
    ) {
        items(items = cache.creaturesList) { creature ->
            if (!isFiltered(creature)) {
                CreatureRowElement(creature, isLoading = creature.name == "Loading...")
            }
        }
    }
}