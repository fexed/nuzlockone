package data

var gamesList: MutableList<Game> = mutableListOf(Game().apply {
    title = "Loading..."
})

class Game(
    var id: Int = -1,
    var title: String = "",
    var isValid: Boolean = false
)