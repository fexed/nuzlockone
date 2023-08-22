package data

import androidx.compose.ui.graphics.Color


class Creature(
    var id: Int = -1,
    var name: String = "MissingNo",
    var type1: Type = Type.UNKNOWN,
    var type2: Type = Type.NONE,
    var isBaby: Boolean = false,
    var isLegendary: Boolean = false,
    var isMithycal: Boolean = false,
    var generation: Int = -1,
    var description: String = "",
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