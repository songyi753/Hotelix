package com.example.hotelix_application.database.Rooms

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "rooms_list_with_reservation")
data class RoomListWithReservation(
    var reserveID: Long = 0L,
    var generatedID: String,
    var roomType: String,
    var status: String,
    var isReservation: Boolean
): Parcelable