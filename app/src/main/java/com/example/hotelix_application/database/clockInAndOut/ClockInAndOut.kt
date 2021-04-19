package com.example.hotelix_application.database.clockInAndOut

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

@Entity(tableName = "clock_in_and_out_table")
data class ClockInAndOut(
        @PrimaryKey(autoGenerate = true)
        var clockID: Long = 0L,

        @ColumnInfo(name = "start_time_milli")
        val clockStartTimeMilli: Long = System.currentTimeMillis(),

        @ColumnInfo(name = "end_time_milli")
        var clockEndTimeMilli: Long = clockStartTimeMilli,

        var duration: Long = clockEndTimeMilli - clockStartTimeMilli,

        var hasClockOut: Boolean = false,

        var staffClockID: String = FirebaseAuth.getInstance().currentUser.uid
)
