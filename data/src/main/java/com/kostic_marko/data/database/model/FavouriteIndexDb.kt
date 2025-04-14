package com.kostic_marko.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = FavouriteIndexDb.ENTITY_NAME)
data class FavouriteIndexDb(
    @PrimaryKey @ColumnInfo(name = ID) val id: Int,
) {

    companion object {
        const val ENTITY_NAME = "favourite_index"
        const val ID = "favourite_index_id"
    }
}