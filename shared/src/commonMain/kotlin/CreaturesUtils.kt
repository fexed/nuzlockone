import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

class Creature {
    val number: Int = -1
    val name: String = "Missigno"
    val type1: Type = Type.UNKNOWN
    val type2: Type = Type.NONE
    val image: String = "compose-multiplatform.xml"
}

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
fun CreatureCard(creature: Creature) {
    Card(modifier = Modifier.wrapContentHeight().fillMaxWidth()) {
        Row(modifier = Modifier.wrapContentHeight().fillMaxWidth()) {
            Image(
                painterResource(creature.image),
                null,
                modifier = Modifier.size(75.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("${creature.number}", fontSize = 8.sp)
                Text(creature.name, fontSize = 16.sp)
                TypePill(creature.type1)
                TypePill(creature.type2)
            }
        }
    }
}

fun getTypeColor(type: Type): Color {
    return when (type) {
        Type.Grass -> Color("7AC74C".toLong(radix = 16))
        Type.Water -> Color("6390F0".toLong(radix = 16))
        Type.Fire -> Color("EE8130".toLong(radix = 16))
        Type.Ground -> Color("E2BF65".toLong(radix = 16))
        Type.Ghost -> Color("735797".toLong(radix = 16))
        Type.Dragon -> Color("6F35FC".toLong(radix = 16))
        Type.Ice -> Color("96D9D6".toLong(radix = 16))
        Type.Rock -> Color("B6A136".toLong(radix = 16))
        Type.Steel -> Color("B7B7CE".toLong(radix = 16))
        Type.Fairy -> Color("D685AD".toLong(radix = 16))
        Type.Electric -> Color("F7D02C".toLong(radix = 16))
        Type.Dark -> Color("705746".toLong(radix = 16))
        Type.Bug -> Color("A6B91A".toLong(radix = 16))
        Type.Poison -> Color("A33EA1".toLong(radix = 16))
        Type.Normal -> Color("A8A77A".toLong(radix = 16))
        Type.Psychic -> Color("F95587".toLong(radix = 16))
        Type.Fighting -> Color("C22E28".toLong(radix = 16))
        Type.Flying -> Color("A98FF3".toLong(radix = 16))
        Type.UNKNOWN -> Color("A8A77A".toLong(radix = 16))
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