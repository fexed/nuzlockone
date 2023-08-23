package network

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import data.Creature
import data.Game
import data.Location
import data.Type

class Cache {
    var numberOfPokemons: MutableState<Int> = mutableStateOf(1)
    var creaturesList: MutableList<Creature> = mutableListOf(Creature().apply {
        name = "Loading..."
        type1 = Type.NONE
    })

    var numberOfLocations: MutableState<Int> = mutableStateOf(1)
    var locationsList: MutableList<Location> = mutableListOf(Location().apply {
        name = "Loading..."
    })

    var numberOfGames: MutableState<Int> = mutableStateOf(1)
    var gamesList: MutableList<Game> = mutableListOf(Game().apply {
        title = "Loading..."
    })

    companion object {
        val instance: Cache by lazy {
            Cache()
        }
    }
}