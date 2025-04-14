package com.kostic_marko.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kostic_marko.data.database.model.MovieDb.Companion.ID
import com.kostic_marko.data.database.model.MovieDb.Companion.OVERVIEW
import com.kostic_marko.data.database.model.MovieDb.Companion.PAGE_ORDINAL
import com.kostic_marko.data.database.model.MovieDb.Companion.POSTER_PATH
import com.kostic_marko.data.database.model.MovieDb.Companion.RELEASE_DATE
import com.kostic_marko.data.database.model.MovieDb.Companion.TITLE
import com.kostic_marko.data.database.model.MovieDb.Companion.VOTE_AVERAGE
import com.kostic_marko.data.database.model.MovieDb.Companion.VOTE_COUNT

@Entity
data class MovieWithFavouriteDb(
    @PrimaryKey @ColumnInfo(name = ID) val id: Int,
    @ColumnInfo(name = TITLE) val title: String,
    @ColumnInfo(name = OVERVIEW) val overview: String,
    @ColumnInfo(name = POSTER_PATH) val posterPath: String,
    @ColumnInfo(name = RELEASE_DATE) val releaseDate: String,
    @ColumnInfo(name = VOTE_AVERAGE) val voteAverage: Float,
    @ColumnInfo(name = VOTE_COUNT) val voteCount: Int,
    @ColumnInfo(name = PAGE_ORDINAL) val pageOrdinal: Int,
    @ColumnInfo(name = FavouriteIndexDb.ID) val favouriteId: Int?,
)