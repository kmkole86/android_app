package com.kostic_marko.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.kostic_marko.data.database.model.FavouriteIndexDb
import com.kostic_marko.data.database.model.MovieDb
import com.kostic_marko.data.database.model.MovieWithFavouriteDb
import com.kostic_marko.data.database.model.MoviesIndexDb
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun _insertMovies(movies: List<MovieDb>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun _insertMoviesIndex(moviesIndex: List<MoviesIndexDb>)

    @Transaction
    suspend fun insertMovies(movies: List<MovieDb>) {
        _insertMovies(movies)
        _insertMoviesIndex(moviesIndex = movies.map { MoviesIndexDb(id = it.id) })
    }

    @Query("DELETE FROM ${MovieDb.ENTITY_NAME} WHERE ${MovieDb.ID} NOT IN (SELECT ${FavouriteIndexDb.ID} FROM ${FavouriteIndexDb.ENTITY_NAME})")
    suspend fun _clearMoviesCache()

    @Query("DELETE FROM ${MoviesIndexDb.ENTITY_NAME}")
    suspend fun _clearMoviesIndex()

    @Transaction
    suspend fun clearMoviesCache() {
        _clearMoviesCache()
        _clearMoviesIndex()
    }

    @Query("SELECT * from ${MovieDb.ENTITY_NAME} INNER JOIN ${MoviesIndexDb.ENTITY_NAME} ON ${MovieDb.ID} = ${MoviesIndexDb.ID} LEFT JOIN ${FavouriteIndexDb.ENTITY_NAME} ON ${MovieDb.ID} = ${FavouriteIndexDb.ID} ORDER BY ${MovieDb.PAGE_ORDINAL}, ${MovieDb.TITLE} ASC")
    fun observeMovies(): Flow<List<MovieWithFavouriteDb>>

    @Query("SELECT * from ${MovieDb.ENTITY_NAME} as Movies INNER JOIN ${FavouriteIndexDb.ENTITY_NAME} AS Favourites ON Movies.id = Favourites.favourite_index_id ORDER BY Movies.page_ordinal, Movies.title ASC")
    fun observeFavouriteMovies(): Flow<List<MovieWithFavouriteDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovieToFavourites(id: FavouriteIndexDb)

    @Query("DELETE FROM ${FavouriteIndexDb.ENTITY_NAME} WHERE ${FavouriteIndexDb.ID}=:id")
    suspend fun removeMovieFromFavourites(id: Int)

    @Query("SELECT EXISTS(SELECT * FROM ${FavouriteIndexDb.ENTITY_NAME} WHERE ${FavouriteIndexDb.ID}=:id)")
    suspend fun getFavouriteStatus(id: Int): Boolean


    @Query("SELECT EXISTS(SELECT * FROM ${FavouriteIndexDb.ENTITY_NAME} WHERE ${FavouriteIndexDb.ID}=:id)")
    fun observeFavouriteStatus(id: Int): Flow<Boolean>
}