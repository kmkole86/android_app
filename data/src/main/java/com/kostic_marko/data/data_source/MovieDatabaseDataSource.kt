package com.kostic_marko.data.data_source

import com.kostic_marko.data.model.MovieData
import com.kostic_marko.data.model.MoviePageData
import com.kostic_marko.data.model.MovieWithFavouriteData
import kotlinx.coroutines.flow.Flow

interface MovieDatabaseDataSource {

    suspend fun cacheMovies(page: MoviePageData)

    suspend fun clearMoviesCache()

    fun observeMovies(): Flow<List<MovieWithFavouriteData>>

    fun getMovies(): List<MovieData>

    fun observeMovieFavourites(): Flow<List<MovieWithFavouriteData>>

    suspend fun changeMovieFavouriteStatus(id: Int): Boolean

    fun observeMovieFavouriteStatus(id: Int): Flow<Boolean>
}