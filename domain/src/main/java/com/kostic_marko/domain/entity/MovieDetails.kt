package com.kostic_marko.domain.entity

data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val releaseDate: String,
    val voteAverage: Float,
    val voteCount: Int,
    val revenue: Int,
    val runtime: Int,
    val status: String,
    val tagline: String,
    val genres: List<MovieGenre>,
    val spokenLanguages: List<SpokenLanguage>,
    val productionCountries: List<ProductionCountry>,
)