package com.example.hotelix_application.database.reservation

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

//room database only have (string, int, float, long)
@Parcelize
@Entity(tableName = "reservation_list_table")
data class ReservationList(
        @PrimaryKey(autoGenerate = true)
        var reserveID: Long = 0L,

        @ColumnInfo(name = "reservation_custom_id")
        var generatedID: String,

        @ColumnInfo(name = "reservation_name")
        val name: String,

        @ColumnInfo(name="reservation_date")
        val reserveDate: String,

        @ColumnInfo(name="room_id")
        var roomId: String,

        @ColumnInfo(name="reservation_contact_number")
        val contactNumber : String
): Parcelable