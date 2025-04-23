package com.kostic_marko.android_app.features.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.kostic_marko.android_app.R
import com.kostic_marko.android_app.features.common.ui.FavouriteStatus
import com.kostic_marko.android_app.features.common.ui.GenericError
import com.kostic_marko.android_app.features.common.ui.GenericLoading
import com.kostic_marko.android_app.features.model.MovieDetailsUiModel

@Composable
fun MovieDetailsScreen(id: Int, modifier: Modifier = Modifier, viewModel: MovieDetailsViewModel) {
    val movieDetails by viewModel.movieDetails.collectAsStateWithLifecycle()
    val favouriteStatus by viewModel.favouriteStatus.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        when (val details = movieDetails) {
            MovieDetailsState.MovieDetailsFailed -> GenericError(onRetry = viewModel::onRetry)
            is MovieDetailsState.MovieDetailsLoaded -> MovieDetails(
                movieDetails = details.movie,
                onFavouriteClicked = viewModel::onChangeFavouriteStatus,
                favouriteStatus = favouriteStatus
            )

            MovieDetailsState.MovieDetailsLoading -> GenericLoading()
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MovieDetails(
    movieDetails: MovieDetailsUiModel,
    favouriteStatus: Boolean,
    onFavouriteClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(
                start = dimensionResource(R.dimen.spacing_2x),
                end = dimensionResource(R.dimen.spacing_2x),
                bottom = dimensionResource(R.dimen.spacing_2x)
            )
            .verticalScroll(rememberScrollState())
    ) {
        GlideImage(
            model = "https://image.tmdb.org/t/p/w500${movieDetails.posterPath}",
            contentDescription = "icon",
            modifier = Modifier
                .padding(top = dimensionResource(R.dimen.spacing_1x))
                .fillMaxWidth()
                .aspectRatio(ratio = 0.67f)
                .shadow(elevation = dimensionResource(R.dimen.spacing_0_5x))
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.spacing_1x)))
        ) {
            it.error(R.drawable.error)
                .placeholder(R.drawable.image)
                .load("https://image.tmdb.org/t/p/w500${movieDetails.posterPath}")
        }
        Row {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = dimensionResource(R.dimen.spacing_2x)),
                text = movieDetails.title,
                textAlign = TextAlign.Start,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            FavouriteStatus(
                onClicked = onFavouriteClicked,
                favouriteStatus = favouriteStatus
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(R.dimen.spacing_1x)),
            text = movieDetails.tagline,
            textAlign = TextAlign.Start,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(R.dimen.spacing_3x)),
            text = movieDetails.overview,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall,
        )
        Row(
            modifier = Modifier.padding(top = dimensionResource(R.dimen.spacing_2x)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Outlined.Star, contentDescription = "Rating")
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = movieDetails.voteAverage.toString(),
                textAlign = TextAlign.Start,
                maxLines = 1,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}