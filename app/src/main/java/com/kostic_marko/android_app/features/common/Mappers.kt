package com.kostic_marko.android_app.features.common

import com.kostic_marko.android_app.features.model.MovieDetailsUiModel
import com.kostic_marko.android_app.features.model.MovieGenreUiModel
import com.kostic_marko.android_app.features.model.MovieUiModel
import com.kostic_marko.android_app.features.model.ProductionCountryUiModel
import com.kostic_marko.android_app.features.model.SpokenLanguageUiModel
import com.kostic_marko.domain.entity.Movie
import com.kostic_marko.domain.entity.MovieDetails
import com.kostic_marko.domain.entity.MovieGenre
import com.kostic_marko.domain.entity.ProductionCountry
import com.kostic_marko.domain.entity.SpokenLanguage

fun Movie.mapToUi() = MovieUiModel(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    isFavourite = isFavourite
)

fun MovieDetails.mapToUi() =
    MovieDetailsUiModel(
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
        genres = genres.map { it.mapToUi() },
        spokenLanguages = spokenLanguages.map { it.mapToUi() },
        productionCountries = productionCountries.map { it.mapToUi() }
    )

fun SpokenLanguage.mapToUi() = SpokenLanguageUiModel(
    englishName = englishName,
    isoCode = isoCode,
    name = name
)

fun MovieGenre.mapToUi() = MovieGenreUiModel(
    id = id,
    name = name
)

fun ProductionCountry.mapToUi() = ProductionCountryUiModel(
    isoCode = isoCode,
    name = name
)