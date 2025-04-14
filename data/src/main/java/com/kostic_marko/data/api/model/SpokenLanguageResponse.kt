package com.kostic_marko.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SpokenLanguageResponse(
    @SerialName(value = "english_name")
    val englishName: String,
    @SerialName(value = "iso_639_1")
    val isoCode: String,
    @SerialName(value = "name")
    val name: String
)