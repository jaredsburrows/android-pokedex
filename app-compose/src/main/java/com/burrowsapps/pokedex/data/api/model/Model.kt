package com.burrowsapps.pokedex.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// Pokemon List
@JsonClass(generateAdapter = true)
data class PokemonListResponse(
  @Json(name = "count") val count: Int = 0,
  @Json(name = "next") val next: String? = "",
  @Json(name = "previous") val previous: String? = "",
  @Json(name = "results") val results: List<PokemonEntry> = emptyList(),
)

@JsonClass(generateAdapter = true)
data class PokemonEntry(
  @Json(name = "name") val name: String = "",
  @Json(name = "url") val url: String = "",
)

// TODO data breakdown
data class PokemonListResult(
  val nextPage: Int?,
  val prevPage: Int?,
  val results: List<PokemonEntry>,
)

// Pokemon Info
@JsonClass(generateAdapter = true)
data class PokemonInfoResponse(
  @Json(name = "abilities") val abilities: List<PokemonAbility> = emptyList(),
  @Json(name = "base_experience") val baseExperience: Int = 0,
  @Json(name = "cries") val cries: PokemonCries = PokemonCries("", ""),
  @Json(name = "forms") val forms: List<PokemonForm> = emptyList(),
  @Json(name = "game_indices") val gameIndices: List<GameIndex> = emptyList(),
  @Json(name = "height") val height: Int = 0,
  @Json(name = "held_items") val heldItems: List<HeldItem> = emptyList(),
  @Json(name = "id") val id: Int = 0,
  @Json(name = "is_default") val isDefault: Boolean = false,
  @Json(name = "location_area_encounters") val locationAreaEncounters: String = "",
  @Json(name = "moves") val moves: List<PokemonMove> = emptyList(),
  @Json(name = "name") val name: String = "",
  @Json(name = "order") val order: Int = 0,
  @Json(name = "species") val species: PokemonSpecies = PokemonSpecies("", ""),
  @Json(name = "sprites") val sprites: PokemonSprites = PokemonSprites(
      "",
      "",
      SpritesOther(OfficialArtwork("")),
  ),
  @Json(name = "stats") val stats: List<PokemonStat> = emptyList(),
  @Json(name = "types") val types: List<PokemonType> = emptyList(),
  @Json(name = "weight") val weight: Int = 0,
)

@JsonClass(generateAdapter = true)
data class PokemonAbility(
  @Json(name = "ability") val ability: PokemonEntry,
  @Json(name = "is_hidden") val isHidden: Boolean,
  @Json(name = "slot") val slot: Int,
)

@JsonClass(generateAdapter = true)
data class PokemonCries(
  @Json(name = "latest") val latest: String,
  @Json(name = "legacy") val legacy: String,
)

@JsonClass(generateAdapter = true)
data class PokemonForm(
  @Json(name = "name") val name: String,
  @Json(name = "url") val url: String,
)

@JsonClass(generateAdapter = true)
data class GameIndex(
  @Json(name = "game_index") val gameIndex: Int,
  @Json(name = "version") val version: PokemonEntry,
)

@JsonClass(generateAdapter = true)
data class HeldItem(
  @Json(name = "item") val item: PokemonEntry,
  @Json(name = "version_details") val versionDetails: List<VersionDetail>,
)

@JsonClass(generateAdapter = true)
data class VersionDetail(
  @Json(name = "rarity") val rarity: Int = 0,
  @Json(name = "version") val version: PokemonEntry = PokemonEntry(),
)

@JsonClass(generateAdapter = true)
data class PokemonMove(
  @Json(name = "move") val move: PokemonEntry = PokemonEntry(),
  @Json(name = "version_group_details") val versionGroupDetails: List<VersionGroupDetail> = emptyList(),
)

@JsonClass(generateAdapter = true)
data class VersionGroupDetail(
  @Json(name = "level_learned_at") val levelLearnedAt: Int = 0,
  @Json(name = "move_learn_method") val moveLearnMethod: PokemonEntry = PokemonEntry(),
  @Json(name = "version_group") val versionGroup: PokemonEntry = PokemonEntry(),
)

@JsonClass(generateAdapter = true)
data class PokemonSpecies(
  @Json(name = "name") val name: String = "",
  @Json(name = "url") val url: String = "",
)

@JsonClass(generateAdapter = true)
data class PokemonSprites(
  // Include other fields as necessary
  @Json(name = "front_default") val frontDefault: String,
  @Json(name = "back_default") val backDefault: String,
  @Json(name = "other") val other: SpritesOther,
  // Include other sprite variations as needed
)

@JsonClass(generateAdapter = true)
data class SpritesOther(
  @Json(name = "official-artwork") val officialArtwork: OfficialArtwork,
  // Include other fields as necessary
)

@JsonClass(generateAdapter = true)
data class OfficialArtwork(
  @Json(name = "front_default") val frontDefault: String,
)

@JsonClass(generateAdapter = true)
data class PokemonStat(
  @Json(name = "base_stat") val baseStat: Int,
  @Json(name = "effort") val effort: Int,
  @Json(name = "stat") val stat: PokemonEntry,
)

@JsonClass(generateAdapter = true)
data class PokemonType(
  @Json(name = "slot") val slot: Int,
  @Json(name = "type") val type: PokemonEntry,
)
