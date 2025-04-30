package com.kostic_marko.android_app

import com.kostic_marko.android_app.common.MainDispatcherRule
import com.kostic_marko.android_app.features.common.mapToUi
import com.kostic_marko.android_app.features.movies.MoviesListState
import com.kostic_marko.android_app.features.movies.MoviesViewModel
import com.kostic_marko.domain.entity.Movie
import com.kostic_marko.domain.entity.result.MoviesError
import com.kostic_marko.domain.entity.result.MoviesResult
import com.kostic_marko.domain.repository.MoviesRepository
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals


private fun movieFromId(id: Int) = Movie(
    id = id,
    title = "title$id",
    overview = "overview$id",
    posterPath = "posterPath$id",
    releaseDate = "releaseDate$id",
    voteAverage = id.toFloat(),
    voteCount = id,
    isFavourite = false
)

private val movies = List(20) {
    movieFromId(it)
}.toImmutableList()
private val moviesUi = movies.map { it.mapToUi() }.toImmutableList()

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesViewModelTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    lateinit var repository: MoviesRepository

    @Before
    fun setup() {
        coEvery { repository.observeMovies() } returns flow {
            emit(movies)
        }
    }

    @Test
    fun `viewModel should emit error state when fetch fails`() = runTest {

        coEvery { repository.fetchMoviePage(text = any(), pageCursor = any()) } answers {
            flow {
                emit(MoviesResult.MoviesLoading(pageCursor = secondArg<Int>()))
                emit(
                    MoviesResult.MoviesFailed(
                        pageCursor = secondArg<Int>(),
                        error = MoviesError.GenericError
                    )
                )
            }
        }


        val viewModel = MoviesViewModel(repository = repository)
        viewModel.onQueryChangedEvent(text = "fun")
        val result = viewModel.movieListState.take(2).toList()

        assertEquals(
            result, listOf(
                MoviesListState.MoviesListStateLoading(pageCursor = 1, movies = persistentListOf()),
                MoviesListState.MoviesListStateFailed(
                    movies = moviesUi,
                    pageCursor = 1,
                    error = MoviesError.GenericError
                )
            )
        )
    }

    @Test
    fun `viewModel should emit success state when fetch succeed`() = runTest {
        coEvery { repository.fetchMoviePage(text = any(), pageCursor = any()) } answers {
            flow {
                emit(MoviesResult.MoviesLoading(pageCursor = secondArg<Int>()))
                emit(
                    MoviesResult.MoviesSuccess(
                        nextPageCursor = secondArg<Int>() + 1
                    )
                )
            }

        }

        val result = MoviesViewModel(repository = repository).movieListState.take(2).toList()

        assertEquals(
            result, listOf(
                MoviesListState.MoviesListStateLoading(pageCursor = 1, movies = persistentListOf()),
                MoviesListState.MoviesListStateSuccess(
                    movies = moviesUi,
                    nextPageCursor = 2,
                )
            )
        )
    }

    //required behaviour: if search query.len<3 app show TopRated movies
    @Test
    fun `viewModel should initially search movies with empty query`() = runTest {

        val searchText = CapturingSlot<String>()
        val pageCursor = CapturingSlot<Int>()

        coEvery {
            repository.fetchMoviePage(
                text = capture(searchText),
                pageCursor = capture(pageCursor)
            )
        } returns flow {
            emit(MoviesResult.MoviesLoading(pageCursor = pageCursor.captured))
            emit(
                MoviesResult.MoviesSuccess(
                    nextPageCursor = pageCursor.captured + 1
                )
            )
        }

        val viewModel = MoviesViewModel(repository = repository)
        val result = viewModel.movieListState.take(2).toList()

        coVerify {
            repository.observeMovies()
            repository.fetchMoviePage(text = "", pageCursor = 1)
        }

        assertEquals(searchText.captured, "")
        assertEquals(pageCursor.captured, 1)
    }

    @Test
    fun `viewModel should search movies`() = runTest {

        coEvery {
            repository.fetchMoviePage(
                text = any(),
                pageCursor = any()
            )
        } answers {
            flow {
                emit(MoviesResult.MoviesLoading(pageCursor = secondArg<Int>()))
                emit(
                    MoviesResult.MoviesSuccess(
                        nextPageCursor = secondArg<Int>() + 1
                    )
                )
            }
        }
        val viewModel = MoviesViewModel(repository = repository)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.movieListState.collect {}
        }
        advanceUntilIdle()
        viewModel.onQueryChangedEvent(text = "fun")
        advanceUntilIdle()
        viewModel.loadNextPage()
        advanceUntilIdle()
        viewModel.loadNextPage()
        advanceUntilIdle()
        viewModel.onQueryChangedEvent(text = "funny")
        advanceUntilIdle()
        viewModel.loadNextPage()
        advanceUntilIdle()
        viewModel.loadNextPage()
        advanceUntilIdle()


        coVerifyOrder {
            repository.fetchMoviePage(text = "fun", pageCursor = 1)
            repository.fetchMoviePage(text = "fun", pageCursor = 2)
            repository.fetchMoviePage(text = "fun", pageCursor = 3)
            repository.fetchMoviePage(text = "funny", pageCursor = 1)
            repository.fetchMoviePage(text = "funny", pageCursor = 2)
            repository.fetchMoviePage(text = "funny", pageCursor = 3)
        }
    }

    @Test
    fun `viewModel should update search text state`() = runTest {

        val viewModel = MoviesViewModel(repository = repository)

        viewModel.onQueryChangedEvent("fun")
        advanceUntilIdle()
        assertEquals(viewModel.query.value.text, "fun")


        viewModel.onQueryChangedEvent("funn")
        advanceUntilIdle()
        assertEquals(viewModel.query.value.text, "funn")


        viewModel.onQueryChangedEvent("funny")
        advanceUntilIdle()
        assertEquals(viewModel.query.value.text, "funny")
    }
}


