package com.kostic_marko.domain.entity.result

import com.kostic_marko.domain.entity.MovieDetails

//TODO make to SingleResult using <T, E>
sealed class MovieDetailsResult {
    object MovieDetailsLoading : MovieDetailsResult()
    data class MovieDetailsSuccess(val movieDetails: MovieDetails) : MovieDetailsResult()
    data class MovieDetailsFailed(val error: MovieDetailsError) : MovieDetailsResult()
}

sealed class MovieDetailsError {
    object GenericError : MovieDetailsError()
    data class ApiError(val errCode: Int? = null) : MovieDetailsError()
}