package com.kostic_marko.android_app.features.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kostic_marko.android_app.features.common.mapToUi
import com.kostic_marko.domain.repository.MoviesRepository
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class FavouritesViewModel(
    private val repository: MoviesRepository,
) : ViewModel() {

    //TODO: add loading and error state
    val state: StateFlow<FavouritesState> =
        repository.observeFavouriteMovies()
            .map { movies ->
                FavouritesState.FavouritesStateLoaded(movies = movies.map { it.mapToUi() }
                    .toPersistentList())
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = FavouritesState.FavouritesStateIdle()
            )


    fun onChangeFavouriteStatus(id: Int) {
        repository.changeMovieFavouriteStatus(
            id
        ).onEach {
            //handle error here, emit toast...
            //if there is a real api call
            //for local impl we are using there is no error possible
        }.launchIn(viewModelScope)
    }
}