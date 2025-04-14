package com.kostic_marko.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kostic_marko.data.database.dao.MovieDao
import com.kostic_marko.data.database.model.FavouriteIndexDb
import com.kostic_marko.data.database.model.MovieDb
import com.kostic_marko.data.database.model.MovieWithFavouriteDb
import com.kostic_marko.data.database.model.MoviesIndexDb

@Database(
    version = 1,
    entities = [
        FavouriteIndexDb::class,
        MoviesIndexDb::class,
        MovieWithFavouriteDb::class,
        MovieDb::class
    ]
)

abstract class AndroidAppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME: String = "android_app_database"
    }

    abstract fun moviesDao(): MovieDao
}