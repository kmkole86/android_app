package com.kostic_marko.android_app.features.movies

import androidx.compose.runtime.Immutable
import com.kostic_marko.android_app.features.model.MovieUiModel
import com.kostic_marko.domain.entity.result.MoviesError
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
sealed class MoviesState {

    companion object {
        fun empty() = MoviesStateLoaded(movies = persistentListOf(), nextPageCursor = 1)
    }

    val hasNextPage: Boolean
        get() = nextPageCursor != null

    val isLoadingNextPage: Boolean
        get() = this is MoviesStateLoading

    abstract val movies: ImmutableList<MovieUiModel>
    abstract val nextPageCursor: Int?

    data class MoviesStateLoading(
        override val movies: ImmutableList<MovieUiModel>,
        override val nextPageCursor: Int?,
    ) : MoviesState()

    data class MoviesStateFailed(
        override val movies: ImmutableList<MovieUiModel>,
        override val nextPageCursor: Int?,
        val error: MoviesError?
    ) : MoviesState()

    data class MoviesStateLoaded(
        override val movies: ImmutableList<MovieUiModel>,
        override val nextPageCursor: Int?,
    ) : MoviesState()
}