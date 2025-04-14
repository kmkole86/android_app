package com.kostic_marko.data.api.common

import com.kostic_marko.data.api.model.MovieDetailsResponse
import com.kostic_marko.data.api.model.MovieGenreResponse
import com.kostic_marko.data.api.model.MovieResponse
import com.kostic_marko.data.api.model.PageResponse
import com.kostic_marko.data.api.model.ProductionCountryResponse
import com.kostic_marko.data.api.model.SpokenLanguageResponse
import com.kostic_marko.data.model.MovieData
import com.kostic_marko.data.model.MovieDetailsData
import com.kostic_marko.data.model.MovieGenreData
import com.kostic_marko.data.model.MoviePageData
import com.kostic_marko.data.model.ProductionCountryData
import com.kostic_marko.data.model.SpokenLanguageData

internal fun PageResponse.mapToData() = with(this) {
    MoviePageData(
        ordinal = ordinal,
        totalPages = totalPages,
        totalResults = totalResults,
        movies = movies.map { it.mapToData() }
    )
}

internal fun MovieResponse.mapToData() =
    with(this) {
        MovieData(
            id = id,
            title = title,
            overview = overview,
            posterPath = posterPath,
            releaseDate = releaseDate,
            voteAverage = voteAverage,
            voteCount = voteCount
        )
    }

internal fun MovieDetailsResponse.mapToData() =
    MovieDetailsData(
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
        genres = genres.map { it.mapToData() },
        spokenLanguages = spokenLanguages.map { it.mapToData() },
        productionCountries = productionCountries.map { it.mapToData() }
    )


internal fun SpokenLanguageResponse.mapToData() = SpokenLanguageData(
    englishName = englishName,
    isoCode = isoCode,
    name = name
)

internal fun MovieGenreResponse.mapToData() = MovieGenreData(
    id = id,
    name = name
)

internal fun ProductionCountryResponse.mapToData() = ProductionCountryData(
    isoCode = isoCode,
    name = name
)