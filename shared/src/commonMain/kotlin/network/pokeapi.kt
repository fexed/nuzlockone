package network

import data.Creature
import data.Type
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import language

class PokeApi {
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

    private suspend fun creatureFromPokemonSpecies(data: PokemonSpecies): Creature {
        val creature = Creature()
        creature.id = data.id
        creature.isBaby = data.is_baby
        creature.isLegendary = data.is_legendary
        creature.isMithycal = data.is_mythical
        creature.generation = mapGenerationToNumber(data.generation.name)

        creature.name = ""
        var defaultName = ""
        for (name in data.names) {
            if (name.language.name == "en") {
                defaultName = name.name

            }

            if (name.language.name == language) {
                creature.name = name.name
                break
            }
        }
        if (creature.name.isEmpty()) creature.name = defaultName

        creature.descriptions = ArrayList()
        for (descr in data.flavor_text_entries) {
            if (descr.language.name == language) {
                creature.descriptions.add(descr.flavor_text)
            }
        }

        for (variety in data.varieties) {
            if (variety.is_default) {
                val defaultVariety = client.get(variety.pokemon.url).body<Pokemon>()
                creature.type1 = mapTypenameToType(defaultVariety.types[0].type.name)
                if (defaultVariety.types.size > 1) {
                    creature.type2 = mapTypenameToType(defaultVariety.types[1].type.name)
                }
                break
            }
        }
        return creature
    }

    suspend fun getCreatureData(name: String): Creature {
        val response = client.get("https://pokeapi.co/api/v2/pokemon-species/$name")
        return creatureFromPokemonSpecies(response.body())
    }

    suspend fun getCreatureData(number: Int): Creature {
        val response = client.get("https://pokeapi.co/api/v2/pokemon-species/$number")
        return creatureFromPokemonSpecies(response.body())
    }

    suspend fun getNumberOfPokemons(): Int {
        val response = client.get("https://pokeapi.co/api/v2/pokemon-species/")
        return response.body<EndpointData>().count
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
class Pokemon(val id: Int, val name: String, val order: Int, val types: List<PokemonType>)

@Serializable
class PokemonType(val slot: Int, val type: NamedAPIResource)

@Serializable
class Type(val id: Int, val name: String, val damage_relations: TypeRelations)

@Serializable
class TypeRelations(val no_damage_to: List<NamedAPIResource>, val half_damage_to: List<NamedAPIResource>, val double_damage_to: List<NamedAPIResource>, val no_damage_from: List<NamedAPIResource>, val half_damage_from: List<NamedAPIResource>, val double_damage_from: List<NamedAPIResource>)