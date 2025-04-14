package com.kostic_marko.data.data_source

import com.kostic_marko.data.model.MovieDetailsData
import com.kostic_marko.data.model.MoviePageData

interface MovieApiDataSource {

    suspend fun fetchPage(cursor: Int): Result<MoviePageData>

    suspend fun fetchMovieDetails(id: Int): Result<MovieDetailsData>
}