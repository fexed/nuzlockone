package data

class Location(
    var id: Int = -1,
    var name: String = "Loading...",
    var regionName: String = "Loading...",
    var areaURLS: MutableList<String> = arrayListOf(),
    var gameIndexes: MutableList<Int> = arrayListOf(),
    var isValid: Boolean = false
)

class Encounter(
    var creature: Creature,
    var chance: Int,
    var typeId: Int,
    var typeName: String,
    var areaName: String,
    var game: Game
) {
    override fun toString(): String {
        return "${creature.name} ($chance% $typeName)"
    }
}