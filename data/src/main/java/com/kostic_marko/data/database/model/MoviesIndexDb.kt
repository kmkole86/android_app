package com.kostic_marko.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = MoviesIndexDb.ENTITY_NAME)
data class MoviesIndexDb(
    @PrimaryKey @ColumnInfo(name = ID) val id: Int,
) {

    companion object {
        const val ENTITY_NAME = "movies_index"
        const val ID = "movies_index_id"
    }
}