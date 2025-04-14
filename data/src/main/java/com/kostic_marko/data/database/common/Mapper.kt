package com.kostic_marko.data.database.common

import com.kostic_marko.data.database.model.MovieDb
import com.kostic_marko.data.database.model.MovieWithFavouriteDb
import com.kostic_marko.data.model.MovieData
import com.kostic_marko.data.model.MovieWithFavouriteData

internal fun MovieWithFavouriteDb.mapToData() = MovieWithFavouriteData(
    movie = MovieData(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount
    ),
    isFavourite = favouriteId != null
)

internal fun MovieDb.mapToData() = MovieData(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount
)

internal fun MovieData.mapToDb(pageOrdinal: Int) = MovieDb(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    pageOrdinal = pageOrdinal
)
