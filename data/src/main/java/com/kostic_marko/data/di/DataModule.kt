package com.kostic_marko.data.di

import com.kostic_marko.data.api.di.apiModule
import com.kostic_marko.data.database.di.databaseModule
import com.kostic_marko.data.repo_impl.MoviesRepositoryImpl
import com.kostic_marko.domain.repository.MoviesRepository
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

sealed class DispatchersAnnotation {
    object IO : DispatchersAnnotation()
    object Unconfined : DispatchersAnnotation()
}

val dataModule = module {
    includes(apiModule, databaseModule)

    single {
        MoviesRepositoryImpl(
            dispatcher = get(named(DispatchersAnnotation.IO.toString())),
            apiDataSource = get(),
            databaseDataSource = get()
        )
    } bind MoviesRepository::class
}