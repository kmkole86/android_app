package com.kostic_marko.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MovieDetailsResponse(
    @SerialName(value = "id")
    val id: Int,
    @SerialName(value = "title")
    val title: String,
    @SerialName(value = "overview")
    val overview: String,
    @SerialName(value = "poster_path")
    val posterPath: String,
    @SerialName(value = "release_date")
    val releaseDate: String,
    @SerialName(value = "vote_average")
    val voteAverage: Float,
    @SerialName(value = "vote_count")
    val voteCount: Int,
    @SerialName(value = "revenue")
    val revenue: Int,
    @SerialName(value = "runtime")
    val runtime: Int,
    @SerialName(value = "status")
    val status: String,
    @SerialName(value = "tagline")
    val tagline: String,
    @SerialName(value = "genres")
    val genres: List<MovieGenreResponse>,
    @SerialName(value = "spoken_languages")
    val spokenLanguages: List<SpokenLanguageResponse>,
    @SerialName(value = "production_countries")
    val productionCountries: List<ProductionCountryResponse>,
)