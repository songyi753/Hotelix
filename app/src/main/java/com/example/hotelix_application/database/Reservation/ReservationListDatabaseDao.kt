package com.example.hotelix_application.database.reservation

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationListDatabaseDao {

    //onConflict = OnConflictStrategy.IGNORE - ignore if user exist
    @Insert
    suspend fun insert(reservation: ReservationList)

    @Update
    suspend fun  update(reservation: ReservationList)

    @Delete
    suspend fun deleteReservation(reservation: ReservationList)

    @Query("SELECT * FROM reservation_list_table ORDER BY reserveID ASC")
    fun getAllReservation(): LiveData<List<ReservationList>>

    @Query("SELECT * FROM reservation_list_table WHERE reserveID = :key")
    suspend fun getReservation(key: Long): ReservationList

    @Query("SELECT reserveID FROM reservation_list_table ORDER BY reserveID DESC LIMIT 1")
    suspend fun getLatestReservationId(): Long

    @Query("SELECT * FROM reservation_list_table WHERE reservation_name LIKE :searchQuery OR reservation_custom_id LIKE :searchQuery")
    fun searchReservation(searchQuery: String): LiveData<List<ReservationList>>
}
