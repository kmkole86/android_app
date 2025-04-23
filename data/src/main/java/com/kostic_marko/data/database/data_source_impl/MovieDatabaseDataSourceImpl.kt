package com.kostic_marko.data.database.data_source_impl

import com.kostic_marko.data.data_source.MovieDatabaseDataSource
import com.kostic_marko.data.database.common.mapToData
import com.kostic_marko.data.database.common.mapToDb
import com.kostic_marko.data.database.dao.MovieDao
import com.kostic_marko.data.database.model.FavouriteIndexDb
import com.kostic_marko.data.model.MovieData
import com.kostic_marko.data.model.MoviePageData
import com.kostic_marko.data.model.MovieWithFavouriteData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal class MovieDatabaseDataSourceImpl(private val dao: MovieDao) : MovieDatabaseDataSource {

    override suspend fun cacheMovies(page: MoviePageData) {
        dao.insertMovies(page.movies.map { it.mapToDb(pageOrdinal = page.ordinal) })
    }

    override suspend fun clearMoviesCache() {
        dao.clearMoviesCache()
    }

    override fun observeMovies(): Flow<List<MovieWithFavouriteData>> =
        dao.observeMovies().distinctUntilChanged().map { movies -> movies.map { it.mapToData() } }

    override fun getMovies(): List<MovieData> = dao.getMovies().map { it.mapToData() }

    override fun observeMovieFavourites(): Flow<List<MovieWithFavouriteData>> =
        dao.observeFavouriteMovies().distinctUntilChanged()
            .map { movies -> movies.map { it.mapToData() } }

    override suspend fun changeMovieFavouriteStatus(id: Int): Boolean {
        if (dao.getFavouriteStatus(id = id))
            dao.removeMovieFromFavourites(id)
        else
            dao.addMovieToFavourites(FavouriteIndexDb(id = id))

        return dao.getFavouriteStatus(id = id)
    }

    override fun observeMovieFavouriteStatus(id: Int): Flow<Boolean> =
        dao.observeFavouriteStatus(id).distinctUntilChanged()
}