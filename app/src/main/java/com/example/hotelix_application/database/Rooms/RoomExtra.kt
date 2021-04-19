package com.example.hotelix_application.database.Rooms

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class RoomExtra(

    var count: Int ,
    var roomType: String


): Parcelable