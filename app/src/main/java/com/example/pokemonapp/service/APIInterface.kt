/*
This file contains the interface functions for the API service:
getPokemonList:     Gets the list of pokemons according to the offset and limit.
getPokemonDetail:   Gets the pokemon detail according to the pokemon id.

@author:    Ana Agnelli
@date:      12/02/2025
@version:   1.0
*/

package com.example.pokemonapp.service

import com.example.pokemonapp.model.PokemonDetail
import com.example.pokemonapp.model.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApiService {
    @GET("pokemon")
    suspend fun getPokemonList(
        //Offset and limit request
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): PokemonResponse

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(@Path("id") id: Int): PokemonDetail
}