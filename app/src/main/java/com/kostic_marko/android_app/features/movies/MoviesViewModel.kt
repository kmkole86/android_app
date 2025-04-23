@file:Suppress("OPT_IN_USAGE")

package com.kostic_marko.android_app.features.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kostic_marko.android_app.features.common.mapToUi
import com.kostic_marko.domain.entity.Movie
import com.kostic_marko.domain.entity.result.MoviesResult
import com.kostic_marko.domain.repository.MoviesRepository
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

//TODO JOB
class MoviesViewModel(
    private val repository: MoviesRepository,
) : ViewModel() {

    private val _query: MutableStateFlow<Query> =
        MutableStateFlow(Query(text = ""))
    val query: StateFlow<Query> = _query

    val movieListState: StateFlow<MoviesListState> = _query.debounce(300)
        .map { if (it.text.length >= 3) it else it.copy(text = "") }
        .flatMapLatest {
            combine<MoviesResult, List<Movie>, MoviesListState>(
                flow = repository.fetchMoviePage(text = it.text, pageCursor = it.pageCursor),
                flow2 = repository.observeMovies()
            ) { fetchResult, cacheResult ->
                when (fetchResult) {
                    is MoviesResult.MoviesLoading -> MoviesListState.MoviesListStateLoading(
                        movies = if (fetchResult.pageCursor == 1) persistentListOf() else cacheResult.map { it.mapToUi() }
                            .toImmutableList(),
                        pageCursor = fetchResult.pageCursor
                    )

                    is MoviesResult.MoviesFailed -> MoviesListState.MoviesListStateFailed(
                        movies = cacheResult.map { it.mapToUi() }.toPersistentList(),
                        pageCursor = fetchResult.pageCursor,
                        error = fetchResult.error
                    )

                    is MoviesResult.MoviesSuccess -> MoviesListState.MoviesListStateSuccess(
                        movies = cacheResult.map { it.mapToUi() }
                            .toImmutableList(),
                        nextPageCursor = fetchResult.nextPageCursor
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MoviesListState.MoviesListStateSuccess(
                movies = persistentListOf(),
                nextPageCursor = 1
            )
        )

    fun onChangeFavouriteStatus(id: Int) {
        repository.changeMovieFavouriteStatus(
            id = id
        ).onEach {
            //handle error here, emit toast...
            //if there is a real api call
            //for local impl we are using there is no error possible
        }.launchIn(viewModelScope)
    }

    fun loadNextPage() {
        movieListState.value.asSuccessOrNull?.nextPageCursor?.let {
            _query.value = query.value.copy(pageCursor = it)
        }
    }

    fun onRetry() {
        if (movieListState.value.isFailedState)
            _query.value = query.value
    }

    fun onQueryChangedEvent(text: String) {
        _query.value = Query(text = text)
    }
}