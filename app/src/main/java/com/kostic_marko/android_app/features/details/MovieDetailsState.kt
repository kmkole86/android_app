package com.kostic_marko.android_app.features.details

import androidx.compose.runtime.Stable
import com.kostic_marko.android_app.features.model.MovieDetailsUiModel

@Stable
sealed class MovieDetailsState {
    object MovieDetailsLoading : MovieDetailsState()
    data class MovieDetailsLoaded(val movie: MovieDetailsUiModel) : MovieDetailsState()
    object MovieDetailsFailed : MovieDetailsState()
}