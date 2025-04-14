package com.kostic_marko.android_app.features.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kostic_marko.android_app.features.common.mapToUi
import com.kostic_marko.domain.entity.result.MovieDetailsResult
import com.kostic_marko.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class MovieDetailsViewModel(
    private val movieId: Int,
    private val repository: MoviesRepository,
) : ViewModel() {


    val favouriteStatus: StateFlow<Boolean> =
        repository.observeMovieFavouriteStatus(movieId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    private val _movieDetails: MutableStateFlow<MovieDetailsResult> =
        MutableStateFlow(MovieDetailsResult.MovieDetailsLoading)
    val movieDetails: StateFlow<MovieDetailsState> =
        _movieDetails.onStart { fetchMovieDetails(movieId) }.map {
            when (it) {
                is MovieDetailsResult.MovieDetailsFailed -> MovieDetailsState.MovieDetailsFailed
                MovieDetailsResult.MovieDetailsLoading -> MovieDetailsState.MovieDetailsLoading
                is MovieDetailsResult.MovieDetailsSuccess -> MovieDetailsState.MovieDetailsLoaded(
                    it.movieDetails.mapToUi()
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MovieDetailsState.MovieDetailsLoading
        )

    private fun fetchMovieDetails(id: Int) {
        repository.fetchMovieDetails(id)
            .onEach {
                _movieDetails.value = it
            }
            .launchIn(
                scope = viewModelScope,
            )
    }

    fun onChangeFavouriteStatus() {
        repository.changeMovieFavouriteStatus(
            movieId
        ).onEach {
            //handle error here, emit toast...
            //if there is a real api call
            //for local impl we are using there is no error possible
        }.launchIn(viewModelScope)
    }

    fun onRetry() {
        fetchMovieDetails(movieId)
    }
}