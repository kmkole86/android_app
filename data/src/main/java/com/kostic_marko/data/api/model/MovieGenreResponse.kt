package com.kostic_marko.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MovieGenreResponse(
    @SerialName(value = "id")
    val id: Int,
    @SerialName(value = "name")
    val name: String,
)