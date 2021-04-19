package com.example.hotelix_application.database.clockInAndOut

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.hotelix_application.database.clockInAndOut.ClockInAndOut

@Dao
interface ClockInAndOutDatabaseDao {
    @Insert
    suspend fun insert(Clock: ClockInAndOut)

    @Update
    suspend fun update(Clock: ClockInAndOut)

    @Query("SELECT * from clock_in_and_out_table WHERE clockID = :key")
    suspend fun get(key: Long): ClockInAndOut?

    @Query("DELETE FROM clock_in_and_out_table")
    suspend fun clear()

    @Query("SELECT * FROM clock_in_and_out_table ORDER BY clockID DESC LIMIT 1")
    suspend fun getClock(): ClockInAndOut?

    @Query("SELECT * FROM clock_in_and_out_table  Where staffClockID = :key ORDER BY clockID DESC" )
    fun getAllClocks(key: String): LiveData<List<ClockInAndOut>>

    @Query("SELECT clockID FROM clock_in_and_out_table ORDER BY clockID DESC LIMIT 1")
    suspend fun getLatestClockId(): Long

    @Query("SELECT * FROM clock_in_and_out_table WHERE clockID = :key")
    suspend fun getClocks(key: Long): ClockInAndOut

}