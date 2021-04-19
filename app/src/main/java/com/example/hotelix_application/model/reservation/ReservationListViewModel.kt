package com.example.hotelix_application.model.reservation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.*
import com.example.hotelix_application.database.HotelixDatabase
import com.example.hotelix_application.database.reservation.ReservationList
import com.example.hotelix_application.database.reservation.ReservationListDatabaseDao
import com.example.hotelix_application.database.reservation.ReservationRepository
import kotlinx.coroutines.launch

//const val RESERVE_ID_CONSTANT = "C"

class ReservationListViewModel(
        dataSource: ReservationListDatabaseDao,
        application: Application
) : ViewModel(){

    val database = dataSource
    val reservationList = database.getAllReservation()
    private val repository: ReservationRepository

    init{
        val reservationDao = HotelixDatabase.getInstance(application).reservationListDao
        repository = ReservationRepository(reservationDao)
    }

    private suspend fun insert(reservation: ReservationList){
        database.insert(reservation)
    }

    private suspend fun update(reservation: ReservationList){
        database.update(reservation)
    }

    private suspend fun delete(reservation: ReservationList){
        database.deleteReservation(reservation)
    }

    private suspend fun getLatestReservationId() : Long{
        return database.getLatestReservationId()
    }

    private suspend fun getLatestReservation(reserveId : Long) : ReservationList{
        return database.getReservation(reserveId)
    }

    fun addReservationToDatabase(reservation: ReservationList){
        viewModelScope.launch {

           insert(reservation)
        }
    }

//    fun createCustomIdAndStore() {
//        viewModelScope.launch {
//            val latestId = getLatestReservationId()
//
//            val latestReservation = getLatestReservation(latestId)
//
//            latestReservation.generatedID = "C$latestId"
//
//            updateReservationToDatabase(latestReservation)
//        }
//    }

    fun updateReservationToDatabase(reservation: ReservationList){ //
        viewModelScope.launch {
            update(reservation)
        }
    }

    fun deleteReservationFromDatabase(reservation: ReservationList){
        viewModelScope.launch {
            delete(reservation)
        }
    }

    fun searchReservation(searchQuery: String) : LiveData<List<ReservationList>>{
        return repository.searchDatabase(searchQuery)
    }

    fun updateRoomType(roomId : String){
        viewModelScope.launch {
            val latestId = getLatestReservationId()

            var latestReservation = getLatestReservation(latestId)

            latestReservation.roomId = roomId

            update(latestReservation)
        }
    }
}