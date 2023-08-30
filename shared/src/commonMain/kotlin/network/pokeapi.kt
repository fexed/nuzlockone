package network

import data.Creature
import data.Encounter
import data.Game
import data.Item
import data.gameNameFix
import data.Location
import data.Type
import data.getGameImageUrl
import getCacheFile
import getPlatformHttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.request.get
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import language

class PokeApi {
    private val baseURL: String = "https://pokeapi.co/api/v2"

    private val client = getPlatformHttpClient().config {
        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        install(HttpCache) {
            publicStorage(getCacheFile())
        }
    }

    private fun mapGenerationToNumber(generationName: String): Int {
        return when (generationName) {
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

    fun mapTypenameToType(typename: String): Type {
        return when (typename) {
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

    fun getLocalizedOrDefaultName(nameList: List<Name>): String {
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

    private suspend fun creatureFromPokemonSpecies(netData: PokemonSpecies): Creature {
        val creature = Creature()
        creature.id = netData.id
        creature.isBaby = netData.is_baby
        creature.isLegendary = netData.is_legendary
        creature.isMithycal = netData.is_mythical
        creature.generation = mapGenerationToNumber(netData.generation.name)

        creature.name = getLocalizedOrDefaultName(netData.names)

        for (descr in netData.flavor_text_entries) {
            if (descr.language.name == language) {
                val version = client.get(descr.version.url).body<Version>()
                val title = gameNameFix(version.id, getLocalizedOrDefaultName(version.names))
                creature.flavorTexts.add(
                    data.FlavorText(
                        descr.language.name, descr.flavor_text + "\n(${title})"
                    )
                )
            }
        }

        coroutineScope {
            for (variety in netData.varieties) {
                if (variety.is_default) {
                    val defaultVariety = client.get(variety.pokemon.url).body<Pokemon>()
                    creature.type1 = mapTypenameToType(defaultVariety.types[0].type.name)
                    if (defaultVariety.types.size > 1) {
                        creature.type2 = mapTypenameToType(defaultVariety.types[1].type.name)
                    }
                    for (id in defaultVariety.game_indices) {
                        creature.gameIndexes.add(gameIdFromGameIndex(id))
                    }

                    for (form_url in defaultVariety.forms) {
                        val form = client.get(form_url.url).body<PokemonForm>()
                        if (form.is_default) {
                            creature.spriteImageUrl = form.sprites.front_default
                            creature.shinySpriteImageUrl = form.sprites.front_shiny
                            creature.backSpriteImageUrl = form.sprites.back_default
                            creature.backShinySpriteImageUrl = form.sprites.back_shiny
                            break
                        }
                    }
                    break
                }
            }
        }

        creature.isValid = true

        return creature
    }

    suspend fun getCreatureData(number: Int): Creature {
        return withContext(Dispatchers.IO) {
            val response = client.get("${baseURL}/pokemon-species/$number")
             creatureFromPokemonSpecies(response.body())
        }
    }

    suspend fun getCreatureData(name: String): Creature {
        return withContext(Dispatchers.IO) {
            val response = client.get("${baseURL}/pokemon-species/$name")
            creatureFromPokemonSpecies(response.body())
        }
    }

    suspend fun getNumberOfPokemons(): Int {
        return withContext(Dispatchers.IO) {
            val response = client.get("${baseURL}/pokemon-species/")
            response.body<EndpointData>().count
        }
    }

    suspend fun getNumberOfLocations(): Int {
        return withContext(Dispatchers.IO) {
            val response = client.get("${baseURL}/location/")
            response.body<EndpointData>().count
        }
    }

    suspend fun getLocationData(number: Int): Location {

        return withContext(Dispatchers.IO) {
            val response = client.get("${baseURL}/location-area/$number")
            val data = response.body<LocationArea>()
            Location().apply {
                id = data.id

                name = getLocalizedOrDefaultName(data.names)

                regionName = ""

                areaURLS.add("${baseURL}/location-area/$number")

                gameIndexes.add(data.game_index)

                isValid = true
            }
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
                        val encounterMethod =
                            client.get(encounterDetail.method.url).body<EncounterMethod>()
                        val encounterMethodName = getLocalizedOrDefaultName(encounterMethod.names)
                        val gameName = getLocalizedOrDefaultName(version.names)
                        val newEncounter = Encounter(
                            creature,
                            encounterDetail.chance,
                            encounterMethod.order,
                            encounterMethodName,
                            Game(id = version.id, title = gameName, isValid = true)
                        )
                        emit(newEncounter)
                    }
                }
            }
        }
    }

    suspend fun getNumberOfGames(): Int {
        return withContext(Dispatchers.IO) {
            val response = client.get("${baseURL}/version/")
            response.body<EndpointData>().count
        }
    }

    suspend fun getGameData(id: Int): Game {
        return withContext(Dispatchers.IO) {
            val version = client.get("${baseURL}/version/$id").body<Version>()
            Game(
                id = id,
                title = gameNameFix(id, getLocalizedOrDefaultName(version.names)),
                imageUrl = getGameImageUrl(id),
                isValid = true
            )
        }
    }

    suspend fun gameIdFromGameIndex(gameIndex: VersionGameIndex): Int {
        return withContext(Dispatchers.IO) {
            val version = client.get(gameIndex.version.url).body<Version>()
            version.id
        }
    }

    suspend fun getNumberOfTypes(): Int {
        return withContext(Dispatchers.IO) {
            val response = client.get("${baseURL}/type/")
            response.body<EndpointData>().count
        }
    }

    suspend fun getTypeData(id: Int): network.Type {
        return withContext(Dispatchers.IO) {
            val type = client.get("${baseURL}/type/$id").body<network.Type>()
            type
        }
    }

    suspend fun getTypeName(id: Int): String {
        return withContext(Dispatchers.IO) {
            val type = getTypeData(id)
            getLocalizedOrDefaultName(type.names)
        }
    }

    suspend fun getNumberOfItems(): Int {
        return withContext(Dispatchers.IO) {
            val response = client.get("${baseURL}/item/")
            response.body<EndpointData>().count
        }
    }

    suspend fun getItemData(id: Int): Item {
        return withContext(Dispatchers.IO) {
            val item = client.get("${baseURL}/item/$id").body<network.Item>()
            Item().apply {
                this.id = item.id
                name = getLocalizedOrDefaultName(item.names)
                cost = item.cost
                imageURL = item.sprites.default
                isValid = true
            }
        }
    }
}

@Serializable
class EndpointData(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<NamedAPIResource>
)

@Serializable
class NamedAPIResource(val name: String, val url: String)

@Serializable
class Name(val name: String, val language: NamedAPIResource)

@Serializable
class FlavorText(
    val flavor_text: String,
    val language: NamedAPIResource,
    val version: NamedAPIResource
)

@Serializable
class Version(
    val id: Int,
    val name: String,
    val names: List<Name>,
    val version_group: NamedAPIResource
)

@Serializable
class PokemonSpecies(
    val id: Int,
    val name: String,
    val order: Int,
    val generation: NamedAPIResource,
    val is_baby: Boolean,
    val is_legendary: Boolean,
    val is_mythical: Boolean,
    val names: List<Name>,
    val varieties: List<PokemonSpeciesVariety>,
    val flavor_text_entries: List<FlavorText>
)

@Serializable
class PokemonSpeciesVariety(val is_default: Boolean, val pokemon: NamedAPIResource)

@Serializable
class Pokemon(
    val id: Int,
    val name: String,
    val order: Int,
    val types: List<PokemonType>,
    val game_indices: List<VersionGameIndex>,
    val forms: List<NamedAPIResource>
)

@Serializable
class PokemonForm(
    val id: Int,
    val name: String,
    val is_default: Boolean,
    val sprites: PokemonFormSprites
)

@Serializable
class PokemonFormSprites(
    val front_default: String,
    val front_shiny: String,
    val back_default: String,
    val back_shiny: String
)

@Serializable
class VersionGameIndex(val game_index: Int, val version: NamedAPIResource)

@Serializable
class PokemonType(val slot: Int, val type: NamedAPIResource)

@Serializable
class Type(
    val id: Int,
    val name: String,
    val damage_relations: TypeRelations,
    val names: List<Name>
)

@Serializable
class TypeRelations(
    val no_damage_to: List<NamedAPIResource>,
    val half_damage_to: List<NamedAPIResource>,
    val double_damage_to: List<NamedAPIResource>,
    val no_damage_from: List<NamedAPIResource>,
    val half_damage_from: List<NamedAPIResource>,
    val double_damage_from: List<NamedAPIResource>
)

@Serializable
class Location(
    val id: Int,
    val name: String,
    val region: NamedAPIResource,
    val names: List<Name>,
    val areas: List<NamedAPIResource>,
    val game_indices: List<GenerationGameIndex>
)

@Serializable
class GenerationGameIndex(val game_index: Int, val generation: NamedAPIResource)

@Serializable
class Generation(
    val id: Int,
    val name: String,
    val names: List<Name>,
    val main_region: NamedAPIResource,
    val moves: List<NamedAPIResource>,
    val pokemon_species: List<NamedAPIResource>,
    val types: List<NamedAPIResource>,
    val version_groups: List<NamedAPIResource>
)

@Serializable
class VersionGroup(
    val id: Int,
    val name: String,
    val order: Int,
    val regions: List<NamedAPIResource>,
    val versions: List<NamedAPIResource>
)

@Serializable
class LocationArea(
    val id: Int,
    val name: String,
    val names: List<Name>,
    val game_index: Int,
    val pokemon_encounters: List<PokemonEncounter>
)

@Serializable
class PokemonEncounter(
    val pokemon: NamedAPIResource,
    val version_details: List<VersionEncounterDetail>
)

@Serializable
class VersionEncounterDetail(
    val version: NamedAPIResource,
    val max_chance: Int,
    val encounter_details: List<network.Encounter>
)

@Serializable
class Encounter(val chance: Int, val method: NamedAPIResource)

@Serializable
class EncounterMethod(val id: Int, val name: String, val order: Int, val names: List<Name>)

@Serializable
class Region(
    val id: Int,
    val name: String,
    val names: List<Name>,
    val main_generation: NamedAPIResource
)

@Serializable
class Item(
    val id: Int,
    val name: String,
    val cost: Int,
    val sprites: ItemSprites,
    val names: List<Name>
)

@Serializable
class ItemSprites(
    val default: String
)