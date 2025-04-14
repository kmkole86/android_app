package com.kostic_marko.android_app.features.favourites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kostic_marko.android_app.R
import com.kostic_marko.android_app.features.common.ui.movie_list.MovieItem
import com.kostic_marko.android_app.features.details.MovieDetailsScreen
import com.kostic_marko.android_app.features.home.DetailsRoute
import com.kostic_marko.android_app.features.home.navigateToDetails
import com.kostic_marko.android_app.features.model.MovieUiModel
import kotlinx.collections.immutable.ImmutableList
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    var shouldShowBack by rememberSaveable { mutableStateOf(false) }
    navController.addOnDestinationChangedListener { _, destination, _ ->
        shouldShowBack = destination.hasRoute<DetailsRoute>()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    "Marko Kostic",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
            },
            navigationIcon = {
                if (shouldShowBack) IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            })
        NavHost(
            navController = navController,
            startDestination = FavouritesRoute,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            composable<FavouritesRoute> {
                FavouritesScreen(onItemClicked = { id ->
                    navController.navigateToDetails(
                        id
                    )
                })
            }
            composable<DetailsRoute> {
                val movieId = it.toRoute<DetailsRoute>().id
                MovieDetailsScreen(
                    id = movieId,
                    viewModel = koinViewModel(parameters = { parametersOf(movieId) })
                )
            }
        }
    }
}

@Composable
fun FavouriteScreenEmpty(modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Outlined.Info,
            modifier = Modifier
                .fillMaxWidth(fraction = 0.5f)
                .aspectRatio(ratio = 1f),
            contentDescription = "No Favourites Icon"
        )
        Text(
            "You have no\nfavourite movies",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun FavouriteScreenList(
    movies: ImmutableList<MovieUiModel>,
    onItemClicked: (Int) -> Unit,
    onFavouriteClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(
            bottom = dimensionResource(id = R.dimen.spacing_2x),
            top = dimensionResource(id = R.dimen.spacing_2x)
        ),
        state = rememberLazyListState(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            movies.size,
            key = { index -> movies[index].id },
            itemContent = { index ->
                MovieItem(
                    movie = movies[index],
                    onItemClicked = onItemClicked,
                    onFavouriteClicked = onFavouriteClicked,
                    modifier = modifier
                )
            })
    }
}

@Composable
fun FavouritesScreen(
    onItemClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavouritesViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state) {
        is FavouritesState.FavouritesStateIdle -> Box(modifier = Modifier.fillMaxWidth()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        is FavouritesState.FavouritesStateLoaded ->
            if (state.movies.isEmpty()) FavouriteScreenEmpty() else
                FavouriteScreenList(
                    state.movies,
                    onItemClicked = onItemClicked,
                    onFavouriteClicked = viewModel::onChangeFavouriteStatus
                )
    }
}