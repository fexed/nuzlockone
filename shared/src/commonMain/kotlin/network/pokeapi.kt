package network

import androidx.compose.ui.graphics.*
import data.Creature
import data.Encounter
import data.Game
import data.Location
import data.Type
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.statement.readBytes
import io.ktor.http.Url
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import language
//import org.jetbrains.skia.Image

class PokeApi {
    val baseURL: String = "https://pokeapi.co/api/v2"

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    private fun mapGenerationToNumber(generationName: String): Int {
        return when(generationName) {
            "generation-i" -> 1
            "generation-ii" -> 2
            "generation-iii" -> 3
            "generation-iv" -> 4
            "generation-v" -> 5
            "generation-vi" -> 6
            "generation-vii" -> 7
            "generation-viii" -> 8
            "generation-ix" -> 9
            "generation-x" -> 10
            "generation-xi" -> 11
            "generation-xii" -> 12
            "generation-xiii" -> 13
            "generation-xiv" -> 14
            "generation-xv" -> 15
            else -> -1
        }
    }

    private fun mapTypenameToType(typename: String): Type {
        return when(typename) {
            "normal" -> Type.Normal
            "fighting" -> Type.Fighting
            "flying" -> Type.Flying
            "poison" -> Type.Poison
            "ground" -> Type.Ground
            "rock" -> Type.Rock
            "bug" -> Type.Bug
            "ghost" -> Type.Ghost
            "steel" -> Type.Steel
            "fire" -> Type.Fire
            "water" -> Type.Water
            "grass" -> Type.Grass
            "electric" -> Type.Electric
            "psychic" -> Type.Psychic
            "ice" -> Type.Ice
            "dragon" -> Type.Dragon
            "dark" -> Type.Dark
            "fairy" -> Type.Fairy
            "unknown" -> Type.UNKNOWN
            "shadow" -> Type.UNKNOWN
            else -> Type.NONE
        }
    }

    private fun getLocalizedOrDefaultName(nameList: List<Name>): String {
        var name = ""
        var defaultName = ""
        for (currentName in nameList) {
            if (currentName.language.name == "en") {
                defaultName = currentName.name
            }

            if (currentName.language.name == language) {
                name = currentName.name
                break
            }
        }
        if (name.isEmpty()) name = defaultName

        return name
    }

    private suspend fun creatureFromPokemonSpecies(data: PokemonSpecies): Creature {
        val creature = Creature()
        creature.id = data.id
        creature.isBaby = data.is_baby
        creature.isLegendary = data.is_legendary
        creature.isMithycal = data.is_mythical
        creature.generation = mapGenerationToNumber(data.generation.name)

        creature.name = getLocalizedOrDefaultName(data.names)

        creature.descriptions = ArrayList()
        for (descr in data.flavor_text_entries) {
            if (descr.language.name == language) {
                creature.descriptions.add(descr.flavor_text + "\n(${getLocalizedOrDefaultName(client.get(descr.version.url).body<Version>().names)})")
            }
        }

        for (variety in data.varieties) {
            if (variety.is_default) {
                val defaultVariety = client.get(variety.pokemon.url).body<Pokemon>()
                creature.type1 = mapTypenameToType(defaultVariety.types[0].type.name)
                if (defaultVariety.types.size > 1) {
                    creature.type2 = mapTypenameToType(defaultVariety.types[1].type.name)
                }
                for (id in defaultVariety.game_indices) {
                    creature.gameIndexes.add(id.game_index)
                }
                break
            }
        }

        creature.isValid = true

        return creature
    }

    suspend fun getCreatureData(number: Int): Creature {
        val response = client.get("${baseURL}/pokemon-species/$number")
        return creatureFromPokemonSpecies(response.body())
    }

    suspend fun getCreatureData(name: String): Creature {
        val response = client.get("${baseURL}/pokemon-species/$name")
        return creatureFromPokemonSpecies(response.body())
    }

    suspend fun getNumberOfPokemons(): Int {
        val response = client.get("${baseURL}/pokemon-species/")
        return response.body<EndpointData>().count
    }

    suspend fun getNumberOfLocations(): Int {
        val response = client.get("${baseURL}/location/")
        return response.body<EndpointData>().count
    }

    suspend fun getLocationData(number: Int): Location {
        val response = client.get("${baseURL}/location/$number")
        val data = response.body<network.Location>()
        return Location().apply {
            id = data.id

            name = getLocalizedOrDefaultName(data.names)

            val region = client.get(data.region.url).body<Region>()
            regionName = getLocalizedOrDefaultName(region.names)

            for (area in data.areas) {
                areaURLS.add(area.url)
            }

            isValid = true
        }
    }

    fun getLocationEncounters(location: Location): Flow<Encounter> = flow {
        for (areaData in location.areaURLS) {
            val area = client.get(areaData).body<LocationArea>()
            for (encounter in area.pokemon_encounters) {
                val creature = getCreatureData(encounter.pokemon.name)
                for (versionDetail in encounter.version_details) {
                    val version = client.get(versionDetail.version.url).body<Version>()
                    for (encounterDetail in versionDetail.encounter_details) {
                        val encounterMethod = client.get(encounterDetail.method.url).body<EncounterMethod>()
                        val encounterMethodName = getLocalizedOrDefaultName(encounterMethod.names)
                        val gameName = getLocalizedOrDefaultName(version.names)
                        val newEncounter = Encounter(creature, encounterDetail.chance, encounterMethod.order, encounterMethodName, Game(id = version.id, title = gameName, isValid = true))
                        emit(newEncounter)
                    }
                }
            }
        }
    }

    suspend fun getNumberOfGames(): Int {
        val response = client.get("${baseURL}/version/")
        return response.body<EndpointData>().count
    }

    suspend fun getGameData(id: Int): Game {
        val version = client.get("${baseURL}/version/$id").body<Version>()
        return Game(id = id, title = getLocalizedOrDefaultName(version.names), isValid = true)
    }

    suspend fun getImage(url: String): ImageBitmap? {
        val bmp: ImageBitmap
        val byteArray = client.get(Url(url)).readBytes()

//        bmp = Image.makeFromEncoded(byteArray).toComposeImageBitmap()
        return null
    }
}

@Serializable
class EndpointData(val count: Int, val next: String?, val previous: String?, val results: List<NamedAPIResource>)

@Serializable
class NamedAPIResource(val name: String, val url: String)

@Serializable
class Name(val name: String, val language: NamedAPIResource)

@Serializable
class FlavorText(val flavor_text: String, val language: NamedAPIResource, val version: NamedAPIResource)

@Serializable
class Version(val id: Int, val name: String, val names: List<Name>, val version_group: NamedAPIResource)

@Serializable
class PokemonSpecies(val id: Int, val name: String, val order: Int, val generation: NamedAPIResource, val is_baby: Boolean, val is_legendary: Boolean, val is_mythical: Boolean, val names: List<Name>, val varieties: List<PokemonSpeciesVariety>, val flavor_text_entries: List<FlavorText>)

@Serializable
class PokemonSpeciesVariety(val is_default: Boolean, val pokemon: NamedAPIResource)

@Serializable
class Pokemon(val id: Int, val name: String, val order: Int, val types: List<PokemonType>, val game_indices: List<VersionGameIndex>)

@Serializable
class VersionGameIndex(val game_index: Int, val version: NamedAPIResource)

@Serializable
class PokemonType(val slot: Int, val type: NamedAPIResource)

@Serializable
class Type(val id: Int, val name: String, val damage_relations: TypeRelations)

@Serializable
class TypeRelations(val no_damage_to: List<NamedAPIResource>, val half_damage_to: List<NamedAPIResource>, val double_damage_to: List<NamedAPIResource>, val no_damage_from: List<NamedAPIResource>, val half_damage_from: List<NamedAPIResource>, val double_damage_from: List<NamedAPIResource>)

@Serializable
class Location(val id: Int, val name: String, val region: NamedAPIResource, val names: List<Name>, val areas: List<NamedAPIResource>)

@Serializable
class LocationArea(val id: Int, val name: String, val names: List<Name>, val pokemon_encounters: List<PokemonEncounter>)

@Serializable
class PokemonEncounter(val pokemon: NamedAPIResource, val version_details: List<VersionEncounterDetail>)

@Serializable
class VersionEncounterDetail(val version: NamedAPIResource, val max_chance: Int, val encounter_details: List<network.Encounter>)

@Serializable
class Encounter(val chance: Int, val method: NamedAPIResource)

@Serializable
class EncounterMethod(val id: Int, val name: String, val order: Int, val names: List<Name>)

@Serializable
class Region(val id: Int, val name: String, val names: List<Name>, val main_generation: NamedAPIResource)