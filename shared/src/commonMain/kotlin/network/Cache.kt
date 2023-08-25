package network

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import data.Creature
import data.Game
import data.Location
import data.NuzlockRun
import data.Type

class Cache {
    var numberOfPokemons: MutableState<Int> = mutableStateOf(1)
    var creaturesList: MutableList<Creature> = mutableListOf(Creature().apply {
        name = "Loading..."
        type1 = Type.NONE
    })

    suspend fun preloadPokemons() {
        val n = try {
            PokeApi().getNumberOfPokemons()
        } catch (e: Exception) {
            println(e.message)
            0
        }
        val creature = Creature().apply {
            id = -1
            name = "Loading..."
            type1 = Type.NONE
            type2 = Type.NONE
            isValid = true
        }
        creaturesList = ArrayList(n)
        for (ix in 0 until n) {
            creature.apply { id = ix + 1 }
            creaturesList.add(ix, creature)
        }
        numberOfPokemons.value = n
//
//        for (ix in 0 until n) {
//            if (!creaturesList[ix].isPreloading) {
//                creaturesList[ix].isPreloading = true
//                coroutineScope {
//                    creaturesList[ix] = try {
//                        PokeApi().getCreatureData(ix + 1)
//                    } catch (e: Exception) {
//                        Creature().apply {
//                            id = -1
//                            name = e.message ?: "Error"
//                            type1 = Type.NONE
//                            type2 = Type.NONE
//                        }
//                    }
//                }
//            }
//        }
    }

    var numberOfLocations: MutableState<Int> = mutableStateOf(1)
    var locationsList: MutableList<Location> = mutableListOf(Location().apply {
        name = "Loading..."
    })

    suspend fun preloadLocations() {
            val n = try {
                PokeApi().getNumberOfLocations()
            } catch (e: Exception) {
                0
            }
            val location = Location()
            locationsList = ArrayList(n)
            for (ix in 0 until n) {
                location.apply {
                    id = ix + 1
                    isValid = true
                }
                locationsList.add(location)
            }
            numberOfLocations.value = n
    }

    var numberOfGames: MutableState<Int> = mutableStateOf(1)
    var gamesList: MutableList<Game> = mutableListOf(Game().apply {
        title = "Loading..."
    })

    suspend fun preloadGames() {
        val n = try {
            PokeApi().getNumberOfGames()
        } catch (e: Exception) {
            0
        }
        val game = Game().apply {
            title = "Loading..."
            isValid = true
        }
        gamesList = ArrayList(n)
        for (ix in 0 until n) {
            gamesList.add(game)
        }
        numberOfGames.value = n
    }

    var numberOfNuzlockes: MutableState<Int> = mutableStateOf(0)
    var nuzlockes: MutableList<NuzlockRun> = mutableListOf()
    suspend fun saveNuzlockeRun(nuzlockRun: NuzlockRun) {

    }

    suspend fun loadNuzlockes() {

    }

    companion object {
        val instance: Cache by lazy {
            Cache()
        }
    }
}