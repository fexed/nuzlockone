package data

import kotlinx.serialization.Serializable

@Serializable
class NuzlockRun (
    var nuzlockeId: Int = 0,
    var gameId: Int = -1,
    var name: String = "",
    var speciesCaughtIds: MutableList<Int> = mutableListOf()
)