import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

class Creature(
    var number: Int = -1,
    var name: String = "MissingNo",
    var type1: Type = Type.UNKNOWN,
    var type2: Type = Type.NONE,
    var image: String = "compose-multiplatform.xml")

enum class Type {
    Grass,
    Water,
    Fire,
    Ground,
    Ghost,
    Dragon,
    Ice,
    Rock,
    Steel,
    Fairy,
    Electric,
    Dark,
    Bug,
    Poison,
    Normal,
    Psychic,
    Fighting,
    Flying,
    UNKNOWN,
    NONE
}


@OptIn(ExperimentalResourceApi::class)
@Composable
fun CreatureCard(creature: Creature, details: String = "", caught: Boolean = false) {
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
                    .border(2.dp, borderColor, CircleShape)
                    .border(6.dp, getTypeColor(creature.type2), CircleShape)
                    .clip(CircleShape)
                    .background(getTypeColor(creature.type1))
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

fun getTypeColor(type: Type): Color {
    return when (type) {
        Type.Grass -> Color("FF7AC74C".toLong(radix = 16))
        Type.Water -> Color("FF6390F0".toLong(radix = 16))
        Type.Fire -> Color("FFEE8130".toLong(radix = 16))
        Type.Ground -> Color("FFE2BF65".toLong(radix = 16))
        Type.Ghost -> Color("FF735797".toLong(radix = 16))
        Type.Dragon -> Color("FF6F35FC".toLong(radix = 16))
        Type.Ice -> Color("FF96D9D6".toLong(radix = 16))
        Type.Rock -> Color("FFB6A136".toLong(radix = 16))
        Type.Steel -> Color("FFB7B7CE".toLong(radix = 16))
        Type.Fairy -> Color("FFD685AD".toLong(radix = 16))
        Type.Electric -> Color("FFF7D02C".toLong(radix = 16))
        Type.Dark -> Color("FF705746".toLong(radix = 16))
        Type.Bug -> Color("FFA6B91A".toLong(radix = 16))
        Type.Poison -> Color("FFA33EA1".toLong(radix = 16))
        Type.Normal -> Color("FFA8A77A".toLong(radix = 16))
        Type.Psychic -> Color("FFF95587".toLong(radix = 16))
        Type.Fighting -> Color("FFC22E28".toLong(radix = 16))
        Type.Flying -> Color("FFA98FF3".toLong(radix = 16))
        Type.UNKNOWN -> Color("FFA8A77A".toLong(radix = 16))
        Type.NONE -> Color.Transparent
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