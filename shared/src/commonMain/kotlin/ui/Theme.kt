package ui

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

private val darkRed = Color(0xffc52018)
private val lightRed = Color(0xffe65a41)
private val darkYellow = Color(0xffde9400)
private val lightYellow = Color(0xfff6bd20)
private val darkGrey = Color(0xff292929)
private val lightGrey = Color(0xff737383)


val DarkColors = darkColors(
    primary = darkRed,
    primaryVariant = lightRed,
    secondary = darkYellow,
    secondaryVariant = lightYellow
)

val LightColors = lightColors(
    primary = lightRed,
    primaryVariant = darkRed,
    secondary = lightYellow,
    secondaryVariant = darkYellow
)