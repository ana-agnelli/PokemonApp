/*
This is the pokemon details screen. It shows the selected pokemon details.

@author:    Ana Agnelli
@date:      12/02/2025
@version:   1.0
*/

package com.example.pokemonapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.pokemonapp.model.PokemonViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    viewModel: PokemonViewModel = viewModel(),
    onBackClick: () -> Unit
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
    val pokemon by viewModel.selectedPokemon.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(pokemonId) {
        viewModel.loadPokemonDetail(pokemonId)
    }

    //Details screen
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = "Pokemon Detail",
                    color = Color(0xFF3B58A8),
                    fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Blue)
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFFFFF))
            )
        }
    ) { padding ->

        //Detail rendering
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            pokemon?.let { pokemonDetail ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xC4FFCC03))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {

                            //API Pokemon Image
                            AsyncImage(
                                model = pokemonDetail.sprites.front_default,
                                contentDescription = "Pokemon image",
                                modifier = Modifier.size(250.dp),
                                contentScale = ContentScale.Fit
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = pokemonDetail.name.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.ROOT
                                ) else it.toString()
                            },
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.pokebluetransp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        //API Pokemon Details
                        Text(
                            text = "Height: ${pokemonDetail.height / 10.0}m",
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorResource(id = R.color.pokebluetransp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Weight: ${pokemonDetail.weight / 10.0}kg",
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorResource(id = R.color.pokebluetransp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        val power = pokemonDetail.stats.find { it.stat.name == "attack" }?.base_stat ?: 0
                        Text(
                            text = "Attack: $power",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.pokebluetransp)
                        )
                    }
                }

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

            }
        }
    }
            //Logo Image
            Image(
                painter = painterResource(id = R.drawable.pokelogo),
                contentDescription = "Pokemon Logo",
                modifier = Modifier
                    .padding(top = 500.dp)
                    .size(120.dp),
                contentScale = ContentScale.Fit
            )
}

