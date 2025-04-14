package com.kostic_marko.data.model

data class MoviePageData(
    val ordinal: Int,
    val totalPages: Int,
    val totalResults: Int,
    val movies: List<MovieData>
)