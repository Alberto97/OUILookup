package org.alberto97.ouilookup.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "oui")
class Oui(
    val oui: String,
    val orgName: String,
    val orgAddress: String,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var ouiId: Long? = null
}