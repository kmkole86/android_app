package com.kostic_marko.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ProductionCountryResponse(
    @SerialName(value = "iso_3166_1")
    val isoCode: String,
    @SerialName(value = "name")
    val name: String,
)