/*
This is the main screen. It contains the following:
Logo.
Search bar:             Searches the entire pokemon list by name (contains).
Pokemon list:           Shows up to 20 pokemons (id and name). On click, navigates to the details screen.
Navigation controls:    Navigates back and forward between the entire pokemon list by 20 pokemons.

@author:    Ana Agnelli
@date:      12/02/2025
@version:   1.0
*/

package com.example.pokemonapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pokemonapp.model.PokemonListItem
import com.example.pokemonapp.model.PokemonViewModel
import java.util.Locale

@Composable
fun PokemonListScreen(
    viewModel: PokemonViewModel = viewModel(),
    onPokemonClick: (Int) -> Unit
) {
    //Error variable
    val error by viewModel.error.collectAsState()

    //Error dialog
    if (error != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("Connection Error") },
            text = { Text(error!!) },
            confirmButton = {
                TextButton(onClick = { viewModel.clearError() }) {
                    Text("OK")
                }
            }
        )
    }

    //Variables used
    var searchQuery by remember { mutableStateOf("") }
    val pokemonList by viewModel.pokemonList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()
    val totalPages by viewModel.totalPages.collectAsState()

    //Screen header
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Logo
                Image(
                    painter = painterResource(id = R.drawable.pokelogo),
                    contentDescription = "Pokemon Logo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    contentScale = ContentScale.FillWidth
                )

                //Search bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { newQuery ->
                        searchQuery = newQuery
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    label = {
                        Text(text = "Search Pokémon...")
                    },
                    trailingIcon = {
                        Row {
                            if (searchQuery.isNotEmpty()){

                                //Search button
                                IconButton(onClick = {
                                    viewModel.filterString = searchQuery
                                    viewModel._currentPage.value = 1
                                    viewModel.applyFilter()

                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search"
                                    )
                                }

                                //Button to clear search
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = {
                                        searchQuery = ""
                                        viewModel.filterString = searchQuery
                                        viewModel._currentPage.value = 1
                                        viewModel.applyFilter()
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Clear search"
                                        )
                                    }
                                }
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(id = R.color.pokebluetransp),
                        unfocusedLabelColor = colorResource(id = R.color.pokeblue),
                        focusedLabelColor = colorResource(id = R.color.pokeblue),
                        cursorColor = colorResource(id = R.color.pokeyellow)
                    )
                )

                //List of items
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(pokemonList) { pokemon ->
                        val pokemonId = pokemon.url.split("/")
                            .dropLast(1)
                            .last()
                            .toInt()

                        PokemonListItem(
                            pokemon = pokemon,
                            pokemonId = pokemonId,
                            onClick = { onPokemonClick(pokemonId) }
                        )
                    }
                }
            }

            //List loading
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.previousPage() },
                enabled = currentPage > 1 && !isLoading
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous page",
                    tint = colorResource(id = R.color.pokeblue)
                )
            }

            Text(
                text = "Page $currentPage of $totalPages",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.pokeblue)
            )

            IconButton(
                onClick = { viewModel.nextPage() },
                enabled = currentPage < totalPages && !isLoading
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next page",
                    tint = colorResource(id = R.color.pokeblue)
                )
            }
        }
    }

}

//List rendering
@Composable
fun PokemonListItem(
    pokemon: PokemonListItem,
    pokemonId: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.pokebluetransp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Id #$pokemonId",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.pokeblue)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = pokemon.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.pokeblue)
                )
            }
        }
    }
}

