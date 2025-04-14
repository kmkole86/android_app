package com.kostic_marko.data.common

import com.kostic_marko.data.model.MovieDetailsData
import com.kostic_marko.data.model.MovieGenreData
import com.kostic_marko.data.model.MovieWithFavouriteData
import com.kostic_marko.data.model.ProductionCountryData
import com.kostic_marko.data.model.SpokenLanguageData
import com.kostic_marko.domain.entity.Movie
import com.kostic_marko.domain.entity.MovieDetails
import com.kostic_marko.domain.entity.MovieGenre
import com.kostic_marko.domain.entity.ProductionCountry
import com.kostic_marko.domain.entity.SpokenLanguage

fun MovieWithFavouriteData.mapToDomain() = Movie(
    id = movie.id,
    title = movie.title,
    overview = movie.overview,
    posterPath = movie.posterPath,
    releaseDate = movie.releaseDate,
    voteAverage = movie.voteAverage,
    voteCount = movie.voteCount,
    isFavourite = isFavourite
)

internal fun MovieDetailsData.mapToDomain() =
    MovieDetails(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        revenue = revenue,
        runtime = runtime,
        status = status,
        tagline = tagline,
        genres = genres.map { it.mapToDomain() },
        spokenLanguages = spokenLanguages.map { it.mapToDomain() },
        productionCountries = productionCountries.map { it.mapToDomain() }
    )


internal fun SpokenLanguageData.mapToDomain() = SpokenLanguage(
    englishName = englishName,
    isoCode = isoCode,
    name = name
)

internal fun MovieGenreData.mapToDomain() = MovieGenre(
    id = id,
    name = name
)

internal fun ProductionCountryData.mapToDomain() = ProductionCountry(
    isoCode = isoCode,
    name = name
)