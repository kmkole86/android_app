package com.kostic_marko.domain.entity.result

//TODO make to ListResult using <T, E>
sealed class MoviesResult {

    abstract val nextPageCursor: Int?

    data class MoviesLoading(
        override val nextPageCursor: Int?
    ) : MoviesResult()

    data class MoviesSuccess(
        override val nextPageCursor: Int?
    ) :
        MoviesResult() {
        companion object {
            fun empty(): MoviesSuccess =
                MoviesSuccess(nextPageCursor = null)
        }
    }

    data class MoviesFailed(
        override val nextPageCursor: Int?,
        val error: MoviesError,
    ) : MoviesResult()
}

sealed class MoviesError {
    object GenericError : MoviesError()
    data class ApiError(val errCode: Int? = null) : MoviesError()
}