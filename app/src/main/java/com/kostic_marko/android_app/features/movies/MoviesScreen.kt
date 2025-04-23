package com.kostic_marko.android_app.features.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kostic_marko.android_app.R
import com.kostic_marko.android_app.features.common.ui.GenericError
import com.kostic_marko.android_app.features.common.ui.GenericLoading
import com.kostic_marko.android_app.features.common.ui.MovieListErrorItem
import com.kostic_marko.android_app.features.common.ui.MovieListLoadingItem
import com.kostic_marko.android_app.features.common.ui.MovieListMovieItem
import com.kostic_marko.android_app.features.common.utils.OnBottomReached
import com.kostic_marko.android_app.features.details.MovieDetailsScreen
import com.kostic_marko.android_app.features.home.DetailsRoute
import com.kostic_marko.android_app.features.home.navigateToDetails
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    var shouldShowBack by rememberSaveable { mutableStateOf(false) }
    navController.addOnDestinationChangedListener { _, destination, _ ->
        shouldShowBack = destination.hasRoute<DetailsRoute>()
    }

    Column(modifier = modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(title = {
            Text(
                "Marko Kostic",
            )
        }, navigationIcon = {
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
            startDestination = MoviesRoute,
            modifier = Modifier.fillMaxWidth()
        ) {
            composable<MoviesRoute> {
                MoviesScreen(onItemClicked = { id ->
                    navController.navigateToDetails(
                        id
                    )
                })
            }
            composable<DetailsRoute> {
                val movieId = it.toRoute<DetailsRoute>().id
                MovieDetailsScreen(
                    id = movieId, viewModel = koinViewModel(parameters = { parametersOf(movieId) })
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    onItemClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MoviesViewModel = koinViewModel(),
) {

    val movieListState by viewModel.movieListState.collectAsStateWithLifecycle()
    val queryState by viewModel.query.collectAsStateWithLifecycle()

    val lazyListState = rememberLazyListState()
    lazyListState.OnBottomReached {
        viewModel::loadNextPage.invoke()
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Search") },
            value = queryState.text,
            onValueChange = viewModel::onQueryChangedEvent,
            placeholder = { Text("Type something...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.spacing_2x))
        )
        if (movieListState.movies.isNotEmpty()) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(
                    bottom = dimensionResource(id = R.dimen.spacing_2x),
                    top = dimensionResource(id = R.dimen.spacing_2x)
                ),
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(id = R.dimen.spacing_2x)),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    movieListState.movies.size,
                    key = { index -> movieListState.movies[index].id },
                    itemContent = { index ->
                        MovieListMovieItem(
                            movie = movieListState.movies[index],
                            onItemClicked = onItemClicked,
                            onFavouriteClicked = viewModel::onChangeFavouriteStatus,
                        )
                    })
                when (val state = movieListState) {
                    is MoviesListState.MoviesListStateFailed -> {
                        item(key = -2) {
                            MovieListErrorItem(onRetryClicked = viewModel::onRetry)
                        }
                    }

                    is MoviesListState.MoviesListStateSuccess -> {
                        if (state.nextPageCursor != null) {
                            item(key = -1) {
                                MovieListLoadingItem()
                            }
                        }
                    }

                    is MoviesListState.MoviesListStateLoading -> {
                        item(key = -1) {
                            MovieListLoadingItem()
                        }
                    }
                }
            }
        } else {
            when (movieListState) {
                is MoviesListState.MoviesListStateFailed -> GenericError(onRetry = viewModel::onRetry)
                is MoviesListState.MoviesListStateLoading -> GenericLoading()
                is MoviesListState.MoviesListStateSuccess -> if (queryState.text.isNotEmpty()) NoResult()
            }
        }
    }
}

@Composable
fun NoResult(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) { Text("No result.") }
}