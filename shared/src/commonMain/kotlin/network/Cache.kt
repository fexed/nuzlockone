package network

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import data.Creature
import data.Game
import data.Location
import data.NuzlockRun
import data.Type
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class Cache {
    var numberOfPokemons: MutableState<Int> = mutableStateOf(1)
    var creaturesList: MutableList<Creature> = mutableListOf(Creature().apply {
        name = "Loading..."
        type1 = Type.NONE
    })

    fun preloadPokemons(scope: CoroutineScope) {
        scope.launch {
            val n = try {
                PokeApi().getNumberOfPokemons()
            } catch (e: Exception) {
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
        }
    }

    var numberOfLocations: MutableState<Int> = mutableStateOf(1)
    var locationsList: MutableList<Location> = mutableListOf(Location().apply {
        name = "Loading..."
    })

    fun preloadLocations(scope: CoroutineScope) {
        scope.launch {
            scope.launch {
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
        }
    }

    var numberOfGames: MutableState<Int> = mutableStateOf(1)
    var gamesList: MutableList<Game> = mutableListOf(Game().apply {
        title = "Loading..."
    })

    fun preloadGames(scope: CoroutineScope) {
        scope.launch {
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
    }

    var numberOfNuzlockes: MutableState<Int> = mutableStateOf(0)
    var nuzlockes: MutableList<NuzlockRun> = mutableListOf()
    suspend fun saveNuzlockeRun(nuzlockRun: NuzlockRun) {

    }

    fun loadNuzlockes(scope: CoroutineScope) {
        scope.launch {

        }
    }

    companion object {
        val instance: Cache by lazy {
            Cache()
        }
    }
}