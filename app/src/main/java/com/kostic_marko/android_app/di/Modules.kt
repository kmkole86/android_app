package com.kostic_marko.android_app.di

import com.kostic_marko.android_app.features.details.MovieDetailsViewModel
import com.kostic_marko.android_app.features.favourites.FavouritesViewModel
import com.kostic_marko.android_app.features.movies.MoviesViewModel
import com.kostic_marko.data.di.DispatchersAnnotation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MoviesViewModel)
    viewModelOf(::FavouritesViewModel)
    viewModel { parameters -> MovieDetailsViewModel(movieId = parameters.get(), get()) }
}

val dispatchersModule = module {
    single<CoroutineDispatcher>(named(DispatchersAnnotation.IO.toString())) { Dispatchers.IO }
    single<CoroutineDispatcher>(named(DispatchersAnnotation.Unconfined.toString())) { Dispatchers.Unconfined }
}