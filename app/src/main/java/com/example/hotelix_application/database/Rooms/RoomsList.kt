package com.example.hotelix_application.database.Rooms

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

//room database only have (string, int, float, long)
@Parcelize
@Entity(tableName = "rooms_list_table")
data class RoomsList(
        @PrimaryKey(autoGenerate = true)
        var roomID: Long = 0L,

        @ColumnInfo(name = "room_custom_id")
        var generatedRoomID: String,

        @ColumnInfo(name = "room_type")
        var roomType: String,

        @ColumnInfo(name = "status")
        var status: String
): Parcelable