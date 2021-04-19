package com.example.hotelix_application.database.Rooms

import androidx.lifecycle.LiveData


class RoomsRepository (private val roomsListDao : RoomsListDatabaseDao){
    fun searchDatabase(searchQuery: String) : LiveData<List<RoomsList>> {
        return roomsListDao.searchRoom(searchQuery)
    }
}