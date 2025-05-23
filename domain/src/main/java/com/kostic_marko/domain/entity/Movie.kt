package com.kostic_marko.domain.entity

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val releaseDate: String,
    val voteAverage: Float,
    val voteCount: Int,
    val isFavourite: Boolean
)