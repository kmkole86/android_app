package com.kostic_marko.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PageResponse(
    @SerialName(value = "page")
    val ordinal: Int,
    @SerialName(value = "results")
    val movies: List<MovieResponse>,
    @SerialName(value = "total_pages")
    val totalPages: Int,
    @SerialName(value = "total_results")
    val totalResults: Int
)