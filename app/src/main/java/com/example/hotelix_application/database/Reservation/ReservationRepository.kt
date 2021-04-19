package com.example.hotelix_application.database.reservation

import androidx.lifecycle.LiveData

class ReservationRepository (private val reservationListDao : ReservationListDatabaseDao){
    fun searchDatabase(searchQuery: String) : LiveData<List<ReservationList>> {
        return reservationListDao.searchReservation(searchQuery)
    }
}