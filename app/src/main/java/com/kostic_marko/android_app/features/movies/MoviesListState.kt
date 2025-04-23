package com.kostic_marko.android_app.features.movies

import androidx.compose.runtime.Stable
import com.kostic_marko.android_app.features.model.MovieUiModel
import com.kostic_marko.domain.entity.result.MoviesError
import kotlinx.collections.immutable.ImmutableList

@Stable
sealed class MoviesListState {

    abstract val movies: ImmutableList<MovieUiModel>

    val isFailedState: Boolean
        get() = this is MoviesListStateFailed

    val asSuccessOrNull: MoviesListStateSuccess?
        get() = this as? MoviesListStateSuccess

    data class MoviesListStateLoading(
        override val movies: ImmutableList<MovieUiModel>,
        val pageCursor: Int,
    ) : MoviesListState()

    data class MoviesListStateFailed(
        override val movies: ImmutableList<MovieUiModel>,
        val pageCursor: Int,
        val error: MoviesError?
    ) : MoviesListState()

    data class MoviesListStateSuccess(
        override val movies: ImmutableList<MovieUiModel>,
        val nextPageCursor: Int?,
    ) : MoviesListState()
}

data class Query(val text: String, val pageCursor: Int = 1)