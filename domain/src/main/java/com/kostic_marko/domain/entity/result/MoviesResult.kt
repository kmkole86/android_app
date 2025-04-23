package com.kostic_marko.domain.entity.result

//TODO make to ListResult using <T, E>
sealed class MoviesResult {

    data class MoviesLoading(
        val pageCursor: Int
    ) : MoviesResult()

    data class MoviesSuccess(
        val nextPageCursor: Int?
    ) :
        MoviesResult()

    data class MoviesFailed(
        val pageCursor: Int,
        val error: MoviesError,
    ) : MoviesResult()
}

sealed class MoviesError {
    object GenericError : MoviesError()
    data class ApiError(val errCode: Int? = null) : MoviesError()
}