package com.example.hotelix_application.model.rooms
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotelix_application.database.HotelixDatabase
import com.example.hotelix_application.database.Rooms.RoomsList
import com.example.hotelix_application.database.Rooms.RoomsListDatabaseDao
import com.example.hotelix_application.database.Rooms.RoomsRepository
import com.example.hotelix_application.database.reservation.ReservationList
import com.example.hotelix_application.database.reservation.ReservationRepository
import kotlinx.coroutines.launch


class RoomsListViewModel(
        dataSource: RoomsListDatabaseDao,
        application: Application
) : ViewModel(){

    private val repository:RoomsRepository
    val database = dataSource
    val checkedInRoomList = database.getAllCheckedInRooms()
    val pendingRoomList = database.getAllPendingRooms()
    val availableVipRoomsList = database.getAllAvailableVipRooms()
    val availableSingleRoomsList = database.getAllAvailableSingleRooms()
    val availablePresidentRoomsList = database.getAllAvailablePresidentRooms()
    val roomsList = database.getAllRooms()
    val vipRoomsList = database.getAllVipRooms()
    val singleRoomsList = database.getAllSingleRooms()
    val presidentRoomsList = database.getAllPresidentRooms()

    init{
        val roomsDao = HotelixDatabase.getInstance(application).roomsListDao
        repository = RoomsRepository(roomsDao)
    }

    private suspend fun insert(roomsList: RoomsList){
        database.insert(roomsList)
    }

    private suspend fun update(roomsList: RoomsList){
        database.update(roomsList)
    }

//    private suspend fun customId(key:Long):RoomsList{
//
//        val room = database.getRooms(key )
//
//        //return RESERVE_ID_CONSTANT + seqId
//        return room
//    }
//
//    private suspend fun generateCustomId(): Long{
//
//        val seqId = database.getLatestRoomsId()
//
//        //return RESERVE_ID_CONSTANT + seqId
//        return seqId
//    }

//    fun createCustomIdAndStore(){
//        viewModelScope.launch {
//            val latestId = generateCustomId()
//            val latestRoom = customId(latestId)
//
//            if(latestRoom.roomType == "single"){
//                latestRoom.generatedRoomID = "S$latestId"
//            }
//            else if(latestRoom.roomType == "vip"){
//                latestRoom.generatedRoomID = "V$latestId"
//            }
//            else{
//                latestRoom.generatedRoomID = "P$latestId"
//            }
//            update(latestRoom)
//        }
//
//    }


    fun addRoomsToDatabase(roomsList: RoomsList){
        viewModelScope.launch {

            insert(roomsList)

        }
    }

    fun updateRoomsToDatabase(roomsList: RoomsList){
        viewModelScope.launch {
            update(roomsList)

        }
    }

    fun searchRoom(searchQuery: String) : LiveData<List<RoomsList>>{
        return repository.searchDatabase(searchQuery)
    }
}