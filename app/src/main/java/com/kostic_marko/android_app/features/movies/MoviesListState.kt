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
        val pageCursor: Int,
        override val movies: ImmutableList<MovieUiModel>,
    ) : MoviesListState()

    data class MoviesListStateFailed(
        val pageCursor: Int,
        override val movies: ImmutableList<MovieUiModel>,
        val error: MoviesError?
    ) : MoviesListState()

    data class MoviesListStateSuccess(
        val nextPageCursor: Int?,
        override val movies: ImmutableList<MovieUiModel>,
    ) : MoviesListState()
}

data class Query(val text: String, val pageCursor: Int = 1)