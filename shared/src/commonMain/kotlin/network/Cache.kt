package network

import data.Creature
import data.Game
import data.Location
import data.Type

class Cache {
    var numberOfPokemons: Int = 1
    var creaturesList: MutableList<Creature> = mutableListOf(Creature().apply {
        name = "Loading..."
        type1 = Type.NONE
    })

    var numberOfLocations: Int = 1
    var locationsList: MutableList<Location> = mutableListOf(Location().apply {
        name = "Loading..."
    })

    var numberOfGames: Int = 1
    var gamesList: MutableList<Game> = mutableListOf(Game().apply {
        title = "Loading..."
    })

    companion object {
        val instance: Cache by lazy {
            Cache()
        }
    }
}