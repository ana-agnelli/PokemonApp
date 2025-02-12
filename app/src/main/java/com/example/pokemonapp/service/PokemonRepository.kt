/*
This file sets the base API URL, creates the API service and declares the API functions.

@author:    Ana Agnelli
@date:      12/02/2025
@version:   1.0
*/

package com.example.pokemonapp.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokemonRepository {
    //API construction
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(PokemonApiService::class.java)

    //List construction
    suspend fun getPokemonList(offset: Int, limit: Int) = api.getPokemonList(offset, limit)

    //Construction of details
    suspend fun getPokemonDetail(id: Int) = api.getPokemonDetail(id)

}
