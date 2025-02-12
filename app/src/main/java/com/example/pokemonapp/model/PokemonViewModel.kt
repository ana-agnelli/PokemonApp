/*
This file contains the functions used by the application:
loadPokemonEntireList:  Loads all pokemons.
clearError:             Resets the error indication.
applyFilter:            Applies the name search filter.
nextPage:               Navigates to the next 20 pokemons.
previousPage:           Navigates to the previous 20 pokemons.
loadPokemonDetail:      Shows the selected pokemon details.

@author:    Ana Agnelli
@date:      12/02/2025
@version:   1.0
*/

package com.example.pokemonapp.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonapp.service.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.ceil

class PokemonViewModel : ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val repository = PokemonRepository()

    private val _pokemonEntireList = MutableStateFlow<List<PokemonListItem>>(emptyList())
    val pokemonEntireList = _pokemonEntireList.asStateFlow()

    private val _pokemonList = MutableStateFlow<List<PokemonListItem>>(emptyList())
    val pokemonList = _pokemonList.asStateFlow()

    private val _pokemonFilteredList = MutableStateFlow<List<PokemonListItem>>(emptyList())
    val pokemonFilteredList = _pokemonFilteredList.asStateFlow()

    private val _selectedPokemon = MutableStateFlow<PokemonDetail?>(null)
    val selectedPokemon = _selectedPokemon.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    var _currentPage = MutableStateFlow(1)
    var currentPage = _currentPage.asStateFlow()

    private var _totalPages = MutableStateFlow(1)
    var totalPages = _totalPages.asStateFlow()

    private val pokemonPerPage = 20

    var filterString = ""

    init {
        loadPokemonEntireList()
    }

    private fun loadPokemonEntireList() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getPokemonList(0, 10000)
                _pokemonEntireList.value = response.results
                applyFilter()
            } catch (e: Exception) {

                //Handle error
                Log.i("Error", "No Internet")
                _error.value = "No internet! Check your connection and reload the app!\nError Message: " + e.message.toString()

            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun applyFilter(){
        val filteredList = pokemonEntireList.value.filter { it.name.contains(filterString, ignoreCase = true) }
        _totalPages.value = ceil(filteredList.size.toFloat() / pokemonPerPage).toInt()
        _pokemonList.value = filteredList.take(pokemonPerPage)
    }

    fun nextPage() {
        if (currentPage.value < totalPages.value) {
            val filteredList = pokemonEntireList.value.filter { it.name.contains(filterString, ignoreCase = true) }
            _pokemonList.value = filteredList.drop(_currentPage.value * pokemonPerPage).take(pokemonPerPage)
            _currentPage.value++
        }
    }

    fun previousPage() {
        if (currentPage.value > 1) {
            val filteredList = pokemonEntireList.value.filter { it.name.contains(filterString, ignoreCase = true) }
            _currentPage.value--
            _pokemonList.value = filteredList.drop(_currentPage.value * pokemonPerPage).take(pokemonPerPage)
        }
    }

    fun loadPokemonDetail(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val pokemon = repository.getPokemonDetail(id)
                _selectedPokemon.value = pokemon
            } catch (e: Exception) {

                //Handle error
                Log.i("Error", "No Internet")
                _error.value = "No internet! Check your connection and try again!\nError Message: " + e.message.toString()
            } finally {
                _isLoading.value = false
            }
        }
    }
}