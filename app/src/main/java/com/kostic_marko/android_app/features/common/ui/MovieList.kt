package com.kostic_marko.android_app.features.common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.kostic_marko.android_app.R
import com.kostic_marko.android_app.features.model.MovieUiModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MovieListMovieItem(
    movie: MovieUiModel,
    onItemClicked: (Int) -> Unit,
    onFavouriteClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(ratio = 3f)
            .fillMaxWidth(),
        onClick = {
            onItemClicked(movie.id)
        },
        shape = RoundedCornerShape(corner = CornerSize(dimensionResource(id = R.dimen.spacing_2x))),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.spacing_0_5x)
        )
    ) {
        Row(
            modifier = Modifier.padding(all = dimensionResource(id = R.dimen.spacing_1x)),
            verticalAlignment = Alignment.Top,
        ) {
            GlideImage(
                model = "https://image.tmdb.org/t/p/w200${movie.posterPath}",
                contentDescription = "icon",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(ratio = 0.67f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.spacing_1x))),
            ) {
                it
                    .error(R.drawable.error)
                    .placeholder(R.drawable.image)
                    .load("https://image.tmdb.org/t/p/w200${movie.posterPath}")
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = dimensionResource(R.dimen.spacing_1x))
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = movie.title,
                    textAlign = TextAlign.Start,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = movie.overview,
                    textAlign = TextAlign.Start,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.Star, contentDescription = "Rating")
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = movie.voteAverage.toString(),
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            FavouriteStatus(
                onClicked = { onFavouriteClicked(movie.id) },
                favouriteStatus = movie.isFavourite,
            )
        }
    }
}

@Composable
fun MovieListLoadingItem(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.spacing_2x)),
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun MovieListErrorItem(
    onRetryClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
            text = "Something went wrong...",
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            onClick = { onRetryClicked() }) {
            Text(text = "Retry")
        }
    }
}