package com.kostic_marko.data.api.data_source_impl

import com.kostic_marko.data.api.common.mapToData
import com.kostic_marko.data.api.model.MovieDetailsResponse
import com.kostic_marko.data.api.model.PageResponse
import com.kostic_marko.data.common.runCatchingCancelable
import com.kostic_marko.data.data_source.MovieApiDataSource
import com.kostic_marko.data.model.MovieDetailsData
import com.kostic_marko.data.model.MoviePageData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

const val PAGE_PARAMETER_KEY = "page"

internal class MovieApiDataSourceImpl(private val client: HttpClient) : MovieApiDataSource {

    override suspend fun fetchPage(cursor: Int): Result<MoviePageData> =
        runCatchingCancelable {
            client.get("top_rated") {
                url {
                    parameters.append(PAGE_PARAMETER_KEY, cursor.toString())
                }
            }.body<PageResponse>().mapToData()
        }

    override suspend fun fetchMovieDetails(id: Int): Result<MovieDetailsData> =
        runCatchingCancelable {
            client.get(id.toString()).body<MovieDetailsResponse>().mapToData()
        }

}