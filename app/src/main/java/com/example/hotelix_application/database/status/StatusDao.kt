package com.example.hotelix_application.database.status

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface StatusDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE) //ignore same room status
    suspend fun addStatus(status: Status)

    @Update
    suspend fun updateStatus(status: Status)

    @Delete
    suspend fun deleteStatus(status: Status)

    @Query("DELETE FROM status_table")
    suspend fun deleteAllStatus()

    @Query("SELECT * FROM status_table ORDER BY roomId ASC")
    fun readAllData(): LiveData<List<Status>>

    @Query("SELECT * FROM status_table WHERE room_name LIKE :searchQuery")
    fun searchStatus(searchQuery: String): LiveData<List<Status>> //Flow<List<Status>>
}