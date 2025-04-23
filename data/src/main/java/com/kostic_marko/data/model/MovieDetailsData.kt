package com.kostic_marko.data.model

data class MovieDetailsData(
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
    val genres: List<MovieGenreData>,
    val spokenLanguages: List<SpokenLanguageData>,
    val productionCountries: List<ProductionCountryData>,
)