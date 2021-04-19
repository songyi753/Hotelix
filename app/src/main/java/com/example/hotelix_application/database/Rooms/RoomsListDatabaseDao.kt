package com.example.hotelix_application.database.Rooms

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.hotelix_application.database.reservation.ReservationList

@Dao
interface RoomsListDatabaseDao {
    //onConflict = OnConflictStrategy.IGNORE - ignore if user exist
    @Insert
    suspend fun insert(roomsList: RoomsList)

    @Update
    suspend fun update(roomsList: RoomsList)

    @Query("SELECT * FROM rooms_list_table WHERE status LIKE 'checkin%' ORDER BY roomID ASC")
    fun getAllCheckedInRooms(): LiveData<List<RoomsList>>

    @Query("SELECT * FROM rooms_list_table WHERE status LIKE 'pending%' ORDER BY roomID ASC")
    fun getAllPendingRooms(): LiveData<List<RoomsList>>

    @Query("SELECT * FROM rooms_list_table ORDER BY roomID ASC")
    fun getAllRooms(): LiveData<List<RoomsList>>

    @Query("SELECT * FROM rooms_list_table WHERE room_type LIKE 'vip%' AND status LIKE 'checkout%' ORDER BY room_type ASC")
    fun getAllAvailableVipRooms(): LiveData<List<RoomsList>>

    @Query("SELECT * FROM rooms_list_table WHERE room_type LIKE 'single%' AND status LIKE 'checkout%' ORDER BY room_type ASC")
    fun getAllAvailableSingleRooms(): LiveData<List<RoomsList>>

    @Query("SELECT * FROM rooms_list_table WHERE room_type LIKE 'president%' AND status LIKE 'checkout%' ORDER BY room_type ASC")
    fun getAllAvailablePresidentRooms(): LiveData<List<RoomsList>>

    @Query("SELECT * FROM rooms_list_table WHERE room_type LIKE 'vip%'  ORDER BY room_type ASC")
    fun getAllVipRooms(): LiveData<List<RoomsList>>

    @Query("SELECT * FROM rooms_list_table WHERE room_type LIKE 'single%' ORDER BY room_type ASC")
    fun getAllSingleRooms(): LiveData<List<RoomsList>>

    @Query("SELECT * FROM rooms_list_table WHERE room_type LIKE 'president%'  ORDER BY room_type ASC")
    fun getAllPresidentRooms(): LiveData<List<RoomsList>>

    @Query("SELECT * FROM rooms_list_table WHERE roomID = :key")
    suspend fun getRooms(key: Long): RoomsList

    @Query("SELECT roomID FROM rooms_list_table ORDER BY roomID DESC LIMIT 1")
    suspend fun getLatestRoomsId(): Long

    @Query("SELECT * FROM rooms_list_table WHERE status LIKE :searchQuery OR room_custom_id LIKE :searchQuery")
    fun searchRoom(searchQuery: String): LiveData<List<RoomsList>>
}

