package com.kostic_marko.android_app.features.model

data class MovieDetailsUiModel(
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
    val genres: List<MovieGenreUiModel>,
    val spokenLanguages: List<SpokenLanguageUiModel>,
    val productionCountries: List<ProductionCountryUiModel>,
)