package data

var locationsList: MutableList<Location> = mutableListOf(Location())

class Location(
    var id: Int = -1,
    var name: String = "Loading...",
    var regionName: String = "Loading...",
    var isValid: Boolean = false
)