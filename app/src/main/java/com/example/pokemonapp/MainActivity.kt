/*
This is the main file. It runs and controls the navigation between screens.

@author:    Ana Agnelli
@date:      12/02/2025
@version:   1.0
*/

package com.example.pokemonapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokemonTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Navigation between screens
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "pokemonList") {
                        composable("pokemonList") {
                            PokemonListScreen(
                                onPokemonClick = { pokemonId ->
                                    navController.navigate("pokemonDetail/$pokemonId")
                                }
                            )
                        }
                        composable(
                            "pokemonDetail/{pokemonId}",
                            arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            PokemonDetailScreen(
                                pokemonId = backStackEntry.arguments?.getInt("pokemonId") ?: 1,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}