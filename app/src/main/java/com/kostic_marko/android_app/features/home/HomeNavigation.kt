package com.kostic_marko.android_app.features.home

import androidx.navigation.NavController
import kotlinx.serialization.Serializable

sealed class HomeRoutes {
    @Serializable
    object MoviesGraphRoute : HomeRoutes()

    @Serializable
    object FavouritesGraphRoute : HomeRoutes()
}

@Serializable
data class DetailsRoute(val id: Int)

fun NavController.navigateToDetails(id: Int) {
    navigate(route = DetailsRoute(id = id))
}