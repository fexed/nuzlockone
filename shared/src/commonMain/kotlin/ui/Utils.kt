package ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import data.Creature
import data.Encounter
import data.Game
import data.Item
import data.Location
import data.NuzlockRun
import data.Type
import data.isGameIdBlacklisted

@Composable
fun shimmerBrush(showShimmer: Boolean = true,targetValue:Float = 1000f): Brush {
    return if (showShimmer) {
        val shimmerColors = listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.6f),
        )

        val transition = rememberInfiniteTransition()
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(800), repeatMode = RepeatMode.Reverse
            )
        )
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnimation.value, y = translateAnimation.value)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent,Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero
        )
    }
}

fun isFiltered(encounter: Encounter): Boolean {
    var isFiltered = isFiltered(encounter.creature)
    if (isFiltered) return isFiltered

    return isFiltered
}

fun isFiltered(creature: Creature): Boolean {
    var isFiltered = false

    val instance = FilterState.instance

    val currentType = instance.currentSelectedType
    val currentGame = instance.currentSelectedGame
    val currentSearchString = instance.currentSearchString

    if (currentSearchString.value != null) {
        if (!creature.name.contains(currentSearchString.value!!)) {
            isFiltered = true
        }
    }

    if (currentType.value != Type.NONE) {
        if (creature.type1 != currentType.value && creature.type2 != currentType.value) {
            isFiltered = true
        }
    }

    if(currentGame.value != -1) {
        if (!creature.gameIndexes.contains(currentGame.value)) {
            isFiltered = true
        }
    }

    if (creature.name == "Loading...") isFiltered = false

    return isFiltered
}

fun isFiltered(nuzlockRun: NuzlockRun): Boolean {
    var isFiltered = false

    val instance = FilterState.instance

    val currentNuzlockRun = instance.currentSelectedNuzlocke
    if (currentNuzlockRun.value != null) {
        if (nuzlockRun.nuzlockeId != currentNuzlockRun.value!!.nuzlockeId) isFiltered = true
    }

    return isFiltered
}

fun isFiltered(location: Location): Boolean {
    var isFiltered = false

    val instance = FilterState.instance

    val currentGame = instance.currentSelectedGame
    val currentSearchString = instance.currentSearchString

    if (currentSearchString.value != null) {
        if (!location.name.contains(currentSearchString.value!!)) {
            isFiltered = true
        }
    }

    if(currentGame.value != -1) {
        if (!location.gameIndexes.contains(currentGame.value)) {
            isFiltered = true
        }
    }

    return isFiltered
}

fun isFiltered(game: Game): Boolean {
    var isFiltered = false
    val instance = FilterState.instance

    val currentSearchString = instance.currentSearchString

    if (currentSearchString.value != null) {
        if (!game.title.contains(currentSearchString.value!!)) {
            isFiltered = true
        }
    }

    if (isGameIdBlacklisted(game.id)) {
        isFiltered = true
    }

    return isFiltered
}

fun isFiltered(item: Item): Boolean {
    var isFiltered = false
    val instance = FilterState.instance

    val currentSearchString = instance.currentSearchString

    if (currentSearchString.value != null) {
        if (!item.name.contains(currentSearchString.value!!)) {
            isFiltered = true
        }
    }

    return isFiltered
}