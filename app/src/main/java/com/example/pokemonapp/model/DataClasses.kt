/*
This file contains the data classes responsible for receiving the information retrieved by the APIs.

@author:    Ana Agnelli
@date:      12/02/2025
@version:   1.0
*/

package com.example.pokemonapp.model

data class PokemonResponse(
    val count: Int,
    val results: List<PokemonListItem>
)

data class PokemonListItem(
    val name: String,
    val url: String
)

data class PokemonDetail(
    val id: Int,
    val name: String,
    val sprites: Sprites,
    val height: Int,
    val weight: Int,
    val stats: List<Stat>
)

data class Stat(
    val base_stat: Int,
    val stat: StatInfo
)

data class StatInfo(
    val name: String
)

data class Sprites(
    val front_default: String
)