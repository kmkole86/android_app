package com.kostic_marko.data.repo_impl

import com.kostic_marko.data.common.mapToDomain
import com.kostic_marko.data.data_source.MovieApiDataSource
import com.kostic_marko.data.data_source.MovieDatabaseDataSource
import com.kostic_marko.domain.entity.Movie
import com.kostic_marko.domain.entity.result.FavouriteStatusResult
import com.kostic_marko.domain.entity.result.MovieDetailsError
import com.kostic_marko.domain.entity.result.MovieDetailsResult
import com.kostic_marko.domain.entity.result.MoviesError
import com.kostic_marko.domain.entity.result.MoviesResult
import com.kostic_marko.domain.repository.MoviesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class MoviesRepositoryImpl(
    private val dispatcher: CoroutineDispatcher,
    private val apiDataSource: MovieApiDataSource,
    private val databaseDataSource: MovieDatabaseDataSource
) : MoviesRepository {
    override fun observeMovies(): Flow<List<Movie>> =
        databaseDataSource.observeMovies()
            .map { movies -> movies.map { it.mapToDomain() } }
            .flowOn(dispatcher)

    override fun fetchMoviePage(cursor: Int): Flow<MoviesResult> = flow<MoviesResult> {
        if (cursor == 1) databaseDataSource.clearMoviesCache()
        apiDataSource.fetchPage(cursor = cursor).onSuccess { page ->
            databaseDataSource.cacheMovies(page)
            emit(
                MoviesResult.MoviesSuccess(
                    nextPageCursor = if (page.ordinal < page.totalPages) page.ordinal + 1 else null
                )
            )
        }.onFailure {
            emit(
                MoviesResult.MoviesFailed(
                    error = MoviesError.GenericError, nextPageCursor = cursor
                )
            )
        }
    }.onStart { emit(MoviesResult.MoviesLoading(nextPageCursor = cursor)) }
        .flowOn(dispatcher)

    override fun changeMovieFavouriteStatus(id: Int): Flow<FavouriteStatusResult> =
        flow<FavouriteStatusResult> {
            emit(
                FavouriteStatusResult.FavouriteStatusSuccess(
                    databaseDataSource.changeMovieFavouriteStatus(
                        id
                    )
                )
            )
        }.onStart { emit(FavouriteStatusResult.FavouriteStatusLoading) }.flowOn(dispatcher)

    override fun observeFavouriteMovies(): Flow<List<Movie>> =
        databaseDataSource.observeMovieFavourites()
            .map { movies -> movies.map { it.mapToDomain() } }
            .flowOn(dispatcher)

    override fun fetchMovieDetails(id: Int): Flow<MovieDetailsResult> = flow<MovieDetailsResult> {
        apiDataSource.fetchMovieDetails(id = id).onSuccess { movieDetails ->
            emit(
                MovieDetailsResult.MovieDetailsSuccess(
                    movieDetails =movieDetails.mapToDomain()
                )
            )
        }.onFailure {
            emit(
                MovieDetailsResult.MovieDetailsFailed(
                    error = MovieDetailsError.GenericError
                )
            )
        }
    }.onStart { emit(MovieDetailsResult.MovieDetailsLoading) }
        .flowOn(dispatcher)

    override fun observeMovieFavouriteStatus(id: Int): Flow<Boolean> =
        databaseDataSource.observeMovieFavouriteStatus(id).flowOn(dispatcher)
}