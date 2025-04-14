package com.kostic_marko.android_app.features.movies

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kostic_marko.android_app.features.common.mapToUi
import com.kostic_marko.domain.entity.Movie
import com.kostic_marko.domain.entity.result.MoviesResult
import com.kostic_marko.domain.repository.MoviesRepository
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
//TODO JOB

class MoviesViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: MoviesRepository,
) : ViewModel() {

    private val _fetchStatus: MutableStateFlow<MoviesResult> =
        MutableStateFlow(MoviesResult.MoviesSuccess(nextPageCursor = 1))

    val state: StateFlow<MoviesState> = combine<MoviesResult, List<Movie>, MoviesState>(
        flow = _fetchStatus,
        flow2 = repository.observeMovies()
    ) { fetchStatus, movies ->

        when (fetchStatus) {
            is MoviesResult.MoviesFailed -> MoviesState.MoviesStateFailed(
                movies = movies.map { it.mapToUi() }.toPersistentList(),
                nextPageCursor = fetchStatus.nextPageCursor,
                error = fetchStatus.error
            )

            is MoviesResult.MoviesLoading -> MoviesState.MoviesStateLoading(movies = movies.map { it.mapToUi() }
                .toPersistentList(), nextPageCursor = fetchStatus.nextPageCursor)

            is MoviesResult.MoviesSuccess -> MoviesState.MoviesStateLoaded(movies = movies.map { it.mapToUi() }
                .toPersistentList(), nextPageCursor = fetchStatus.nextPageCursor)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MoviesState.empty()
    )

    fun loadNextPage() {
        state.value.nextPageCursor?.let { nextPageCursor ->
            repository.fetchMoviePage(cursor = nextPageCursor)
                .onEach {
                    _fetchStatus.value = it
                }
                .launchIn(viewModelScope)
        }
    }

    fun onChangePlaceFavouriteStatus(id: Int) {
        repository.changeMovieFavouriteStatus(
            id = id
        ).onEach {
            //handle error here, emit toast...
            //if there is a real api call
            //for local impl we are using there is no error possible
        }.launchIn(viewModelScope)
    }

    fun onRetry() {
        loadNextPage()
    }
}