package com.kostic_marko.android_app.features.favourites

import androidx.compose.runtime.Immutable
import com.kostic_marko.android_app.features.model.MovieUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
sealed class FavouritesState {

    abstract val movies: ImmutableList<MovieUiModel>

    data class FavouritesStateLoaded(
        override val movies: ImmutableList<MovieUiModel>,
    ) : FavouritesState()

    data class FavouritesStateIdle(
        override val movies: ImmutableList<MovieUiModel> = persistentListOf(),
    ) : FavouritesState()
}