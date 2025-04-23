package com.kostic_marko.domain.repository

import com.kostic_marko.domain.entity.Movie
import com.kostic_marko.domain.entity.result.FavouriteStatusResult
import com.kostic_marko.domain.entity.result.MovieDetailsResult
import com.kostic_marko.domain.entity.result.MoviesResult
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun observeMovies(): Flow<List<Movie>>

    fun fetchMoviePage(text: String, pageCursor: Int): Flow<MoviesResult>

    fun changeMovieFavouriteStatus(id: Int): Flow<FavouriteStatusResult>

    fun observeFavouriteMovies(): Flow<List<Movie>>

    fun fetchMovieDetails(id: Int): Flow<MovieDetailsResult>

    fun observeMovieFavouriteStatus(id: Int): Flow<Boolean>
}