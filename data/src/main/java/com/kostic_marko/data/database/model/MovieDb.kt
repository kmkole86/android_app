package com.kostic_marko.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = MovieDb.ENTITY_NAME)
data class MovieDb(
    @PrimaryKey @ColumnInfo(name = ID) val id: Int,
    @ColumnInfo(name = TITLE) val title: String,
    @ColumnInfo(name = OVERVIEW) val overview: String,
    @ColumnInfo(name = POSTER_PATH) val posterPath: String?,
    @ColumnInfo(name = RELEASE_DATE) val releaseDate: String,
    @ColumnInfo(name = VOTE_AVERAGE) val voteAverage: Float,
    @ColumnInfo(name = VOTE_COUNT) val voteCount: Int,
    @ColumnInfo(name = PAGE_ORDINAL) val pageOrdinal: Int
) {

    companion object {
        const val ENTITY_NAME = "movie_local"
        const val ID = "id"
        const val TITLE = "title"
        const val OVERVIEW = "overview"
        const val POSTER_PATH = "poster_path"
        const val RELEASE_DATE = "release_date"
        const val VOTE_AVERAGE = "vote_average"
        const val VOTE_COUNT = "vote_count"
        const val PAGE_ORDINAL = "page_ordinal"
    }
}