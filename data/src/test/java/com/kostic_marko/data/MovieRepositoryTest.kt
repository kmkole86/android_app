package com.kostic_marko.data

import com.kostic_marko.data.data_source.MovieApiDataSource
import com.kostic_marko.data.data_source.MovieDatabaseDataSource
import com.kostic_marko.data.model.MovieData
import com.kostic_marko.data.model.MoviePageData
import com.kostic_marko.data.repo_impl.MoviesRepositoryImpl
import com.kostic_marko.domain.entity.result.MoviesError
import com.kostic_marko.domain.entity.result.MoviesResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

private fun movieFromId(id: Int) = MovieData(
    id = id,
    title = "title$id",
    overview = "overview$id",
    posterPath = "posterPath$id",
    releaseDate = "releaseDate$id",
    voteAverage = id.toFloat(),
    voteCount = id,
)

private val moviesList = List(20) {
    movieFromId(it)
}.toImmutableList()

private fun pageData(
    ordinal: Int = 1,
    totalPages: Int = 10,
    totalResults: Int = 200,
    movies: List<MovieData> = moviesList
) = MoviePageData(
    ordinal = ordinal,
    totalPages = totalPages,
    totalResults = totalResults,
    movies = movies
)


@ExperimentalCoroutinesApi
class MovieRepositoryTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var apiDataSource: MovieApiDataSource

    @MockK(relaxed = true)
    lateinit var databaseDataSource: MovieDatabaseDataSource

    @Test
    fun `repository should return MoviesLoading and MoviesSuccess if fetch is successful`() =
        runTest {
            coEvery {
                apiDataSource.fetchPage(
                    text = any(),
                    pageCursor = any()
                )
            } returns Result.success(
                pageData(ordinal = 5)
            )

            val result = MoviesRepositoryImpl(
                dispatcher = UnconfinedTestDispatcher(testScheduler),
                apiDataSource = apiDataSource,
                databaseDataSource = databaseDataSource
            ).fetchMoviePage(text = "fun", pageCursor = 5).take(2).toList()

            assertEquals(result[0], MoviesResult.MoviesLoading(pageCursor = 5))
            assertEquals(result[1], MoviesResult.MoviesSuccess(nextPageCursor = 6))
            coVerify {
                apiDataSource.fetchPage(text = "fun", pageCursor = 5)
            }
        }

    @Test
    fun `repository should return MoviesLoading and MoviesFailed if fetch is unsuccessful`() =
        runTest {
            coEvery {
                apiDataSource.fetchPage(
                    text = any(),
                    pageCursor = any()
                )
            } returns Result.failure(
                Exception()
            )

            val result = MoviesRepositoryImpl(
                dispatcher = UnconfinedTestDispatcher(testScheduler),
                apiDataSource = apiDataSource,
                databaseDataSource = databaseDataSource
            ).fetchMoviePage(text = "fun", pageCursor = 5).take(2).toList()

            assertEquals(result[0], MoviesResult.MoviesLoading(pageCursor = 5))
            assertEquals(
                result[1],
                MoviesResult.MoviesFailed(error = MoviesError.GenericError, pageCursor = 5)
            )
            coVerify {
                apiDataSource.fetchPage(text = "fun", pageCursor = 5)
            }
        }

    @Test
    fun `repository should cache movies when fetch is successful`() = runTest {
        coEvery {
            apiDataSource.fetchPage(
                text = any(),
                pageCursor = any()
            )
        } returns Result.success(
            pageData(ordinal = 5)
        )

        MoviesRepositoryImpl(
            dispatcher = UnconfinedTestDispatcher(testScheduler),
            apiDataSource = apiDataSource,
            databaseDataSource = databaseDataSource
        ).fetchMoviePage(text = "fun", pageCursor = 5).collect {}

        coVerify {
            databaseDataSource.cacheMovies(page = pageData(ordinal = 5))
        }
    }

    @Test
    fun `repository should change movie favourite status`() = runTest {

        MoviesRepositoryImpl(
            dispatcher = UnconfinedTestDispatcher(testScheduler),
            apiDataSource = apiDataSource,
            databaseDataSource = databaseDataSource
        ).changeMovieFavouriteStatus(id = 5).collect {}

        coVerify {
            databaseDataSource.changeMovieFavouriteStatus(id = 5)
        }
    }
}