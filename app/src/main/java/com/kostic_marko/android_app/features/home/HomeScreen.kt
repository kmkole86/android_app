package com.kostic_marko.android_app.features.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kostic_marko.android_app.features.favourites.FavouritesGraph
import com.kostic_marko.android_app.features.movies.MoviesGraph

@Composable
fun HomeGraph(modifier: Modifier = Modifier) {
    val homeNavController = rememberNavController()
    var homeDestination by remember { mutableStateOf<HomeRoutes>(HomeRoutes.MoviesGraphRoute) }
    homeNavController.addOnDestinationChangedListener { _, destination, _ ->
        homeDestination = if (destination.hasRoute<HomeRoutes.MoviesGraphRoute>()) {
            HomeRoutes.MoviesGraphRoute
        } else {
            HomeRoutes.FavouritesGraphRoute
        }
    }
    Scaffold(bottomBar = {
        NavigationBar {
            NavigationBarItem(
                icon = {
                    Icon(
                        if (homeDestination is HomeRoutes.MoviesGraphRoute) Icons.Filled.PlayArrow else Icons.Outlined.PlayArrow,
                        contentDescription = "Movies"
                    )
                },
                label = { Text("Movies") },
                selected = homeDestination is HomeRoutes.MoviesGraphRoute,
                onClick = {
                    homeNavController.navigate(HomeRoutes.MoviesGraphRoute) {
                        popUpTo(homeNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
            NavigationBarItem(
                icon = {
                    Icon(
                        if (homeDestination is HomeRoutes.FavouritesGraphRoute) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favourites"
                    )
                },
                label = { Text("Favourites") },
                selected = homeDestination is HomeRoutes.FavouritesGraphRoute,
                onClick = {
                    homeNavController.navigate(HomeRoutes.FavouritesGraphRoute) {
                        popUpTo(homeNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }) { innerPadding ->
        NavHost(
            navController = homeNavController,
            startDestination = HomeRoutes.MoviesGraphRoute,
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            composable<HomeRoutes.MoviesGraphRoute> {
                MoviesGraph()
            }
            composable<HomeRoutes.FavouritesGraphRoute> {
                FavouritesGraph()
            }
        }
    }
}