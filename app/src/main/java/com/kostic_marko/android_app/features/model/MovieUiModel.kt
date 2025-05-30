package com.kostic_marko.android_app.features.model

data class MovieUiModel(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val releaseDate: String,
    val voteAverage: Float,
    val voteCount: Int,
    val isFavourite: Boolean
)