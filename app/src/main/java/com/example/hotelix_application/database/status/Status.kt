package com.example.hotelix_application.database.status

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "status_table")
data class Status(
    @PrimaryKey(autoGenerate = true)
    var roomId: Long = 0L,

    @ColumnInfo(name = "room_cleaning_status")
    var roomCleaningStatus: Int = 1,

    @ColumnInfo(name = "room_name")
    var roomName: String,

    @ColumnInfo(name = "assign_to_cleaner")
    var cleanerName: String?
): Parcelable