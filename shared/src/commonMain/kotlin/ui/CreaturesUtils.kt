package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.Creature
import data.Type
import data.getTypeColor
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


@OptIn(ExperimentalResourceApi::class)
@Composable
fun CreatureRowElement(creature: Creature, details: String = "", caught: Boolean = false) {
    var areDetailsVisible by mutableStateOf(false)
    val borderColor = if (caught) Color.Green else Color.Transparent

    Card(modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(8.dp).clickable {
        if (details.isNotEmpty()) areDetailsVisible = !areDetailsVisible
    }) {
        Row(modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(4.dp)) {
            Image(
                painterResource(creature.image),
                null,
                modifier = Modifier.size(75.dp)
                    .clip(CircleShape)
                    .border(2.dp, borderColor, CircleShape)
                    .background(Brush.radialGradient(listOf(getTypeColor(creature.type1), getTypeColor(
                        if (creature.type2 != Type.NONE) creature.type2 else creature.type1)), radius = 125f))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("${creature.number}", fontSize = 10.sp)
                Text(creature.name, fontSize = 16.sp)
                Row {
                    TypePill(creature.type1)
                    Spacer(modifier = Modifier.width(4.dp))
                    TypePill(creature.type2)
                }

                AnimatedVisibility(areDetailsVisible) {
                    Text(details)
                }
            }
        }
    }
}
@OptIn(ExperimentalResourceApi::class)
@Composable
fun CreatureCard(creature: Creature, details: String = "", caught: Boolean = false) {
    var areDetailsVisible by mutableStateOf(false)
    val borderColor = if (caught) Color.Green else Color.Transparent

    Card(modifier = Modifier.wrapContentHeight().width(150.dp).padding(8.dp).clickable {
        if (details.isNotEmpty()) areDetailsVisible = !areDetailsVisible
    }) {
        Column(modifier = Modifier.wrapContentHeight().wrapContentWidth().padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${creature.number}", fontSize = 10.sp)
            Text(creature.name, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painterResource(creature.image),
                null,
                modifier = Modifier.size(75.dp)
                    .clip(CircleShape)
                    .border(2.dp, borderColor, CircleShape)
                    .background(Brush.radialGradient(listOf(getTypeColor(creature.type1), getTypeColor(
                        if (creature.type2 != Type.NONE) creature.type2 else creature.type1)), radius = 125f))
            )
            Spacer(modifier = Modifier.width(8.dp))
            TypePill(creature.type1)
            Spacer(modifier = Modifier.width(4.dp))
            TypePill(creature.type2)

            AnimatedVisibility(areDetailsVisible) {
                Text(details)
            }
        }
    }
}
@Composable
fun TypePill(type: Type) {
    val color = getTypeColor(type)

    if (type != Type.NONE) {
        OutlinedButton(
            onClick = {},
            border = BorderStroke(1.dp, color),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = color)
        ) { Text("$type") }
    }
}