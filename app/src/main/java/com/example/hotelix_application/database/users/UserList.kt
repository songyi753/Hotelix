package com.example.hotelix_application.database.users

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

//Firebase
@Parcelize
@Entity
data class UserList (
        var name : String,
        var position : String,
        var userEmail : String,
        var userId : String
): Parcelable

