package data

class Location(
    var id: Int = -1,
    var name: String = "Loading...",
    var regionName: String = "Loading...",
    var areaURLS: MutableList<String> = arrayListOf(),
    var isValid: Boolean = false
)

class Encounter(
    var creature: Creature,
    var chance: Int,
    var type: String,
    var game: Game
) {
    override fun toString(): String {
        return "${creature.name} ($chance% $type in ${game.title})"
    }
}