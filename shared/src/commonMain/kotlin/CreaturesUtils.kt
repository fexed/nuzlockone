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
    Electro,
    Dark,
    Bug,
    Poison,
    Normal,
    Psychic,
    Fighting,
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

@Composable
fun TypePill(type: Type) {
    val color = when (type) {
        Type.Grass -> Color(0.0f, 1.0f, 0.0f)
        Type.Water -> Color(0.0f, 1.0f, 0.0f)
        Type.Fire -> Color(0.0f, 1.0f, 0.0f)
        Type.Ground -> Color(0.0f, 1.0f, 0.0f)
        Type.Ghost -> Color(0.0f, 1.0f, 0.0f)
        Type.Dragon -> Color(0.0f, 1.0f, 0.0f)
        Type.Ice -> Color(0.0f, 1.0f, 0.0f)
        Type.Rock -> Color(0.0f, 1.0f, 0.0f)
        Type.Steel -> Color(0.0f, 1.0f, 0.0f)
        Type.Fairy -> Color(0.0f, 1.0f, 0.0f)
        Type.Electro -> Color(0.0f, 1.0f, 0.0f)
        Type.Dark -> Color(0.0f, 1.0f, 0.0f)
        Type.Bug -> Color(0.0f, 1.0f, 0.0f)
        Type.Poison -> Color(0.0f, 1.0f, 0.0f)
        Type.Normal -> Color(0.0f, 1.0f, 0.0f)
        Type.Psychic -> Color(0.0f, 1.0f, 0.0f)
        Type.Fighting -> Color(0.0f, 1.0f, 0.0f)
        Type.UNKNOWN -> Color(0.0f, 1.0f, 0.0f)
        Type.NONE -> Color.Transparent
    }

    if (type != Type.NONE) {
        OutlinedButton(
            onClick = {},
            border = BorderStroke(1.dp, color),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = color)
        ) { Text("$type") }
    }
}