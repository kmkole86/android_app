package com.kostic_marko.data.database.di

import androidx.room.Room
import com.kostic_marko.data.data_source.MovieDatabaseDataSource
import com.kostic_marko.data.database.AndroidAppDatabase
import com.kostic_marko.data.database.AndroidAppDatabase.Companion.DATABASE_NAME
import com.kostic_marko.data.database.dao.MovieDao
import com.kostic_marko.data.database.data_source_impl.MovieDatabaseDataSourceImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {
    single<AndroidAppDatabase> {
        Room.databaseBuilder(
            androidApplication(),
            AndroidAppDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }

    singleOf(::MovieDatabaseDataSourceImpl) bind MovieDatabaseDataSource::class

    single<MovieDao> {
        get<AndroidAppDatabase>().moviesDao()
    }
}