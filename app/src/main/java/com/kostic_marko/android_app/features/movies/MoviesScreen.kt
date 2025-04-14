package com.kostic_marko.android_app.features.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.kostic_marko.android_app.features.common.ui.movie_list.ErrorItem
import com.kostic_marko.android_app.features.common.ui.movie_list.LoadingItem
import com.kostic_marko.android_app.features.common.ui.movie_list.MovieItem
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

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(  "Marko Kostic",
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
    val lazyListState = rememberLazyListState()
    lazyListState.OnBottomReached {
        viewModel::loadNextPage.invoke()
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//    ) {
//        TopAppBar(
//            title = {
//                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart)
//                { Text("Marko Kostic", textAlign = TextAlign.Left) }
//            },
//        )
    LazyColumn(
        contentPadding = PaddingValues(
            bottom = dimensionResource(id = R.dimen.spacing_2x),
            top = dimensionResource(id = R.dimen.spacing_2x)
        ),
        state = lazyListState,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.spacing_2x)),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(state.movies.size, key = { index -> state.movies[index].id }, itemContent = { index ->
            MovieItem(
                movie = state.movies[index],
                onItemClicked = onItemClicked,
                onFavouriteClicked = viewModel::onChangePlaceFavouriteStatus,
                modifier = modifier
            )
        })
        when (state) {
            is MoviesState.MoviesStateFailed -> {
                item {
                    ErrorItem(
                        onRetryClicked = viewModel::onRetry, modifier = modifier
                    )
                }
            }

            is MoviesState.MoviesStateLoaded -> {
                if (state.hasNextPage) {
                    item {
                        LoadingItem(modifier = modifier)
                    }
                }
            }

            is MoviesState.MoviesStateLoading -> {
                item {
                    LoadingItem(modifier = modifier)
                }
            }
        }
    }
//    }
}