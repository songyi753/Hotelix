package com.example.hotelix_application.model.reservation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hotelix_application.database.reservation.ReservationListDatabaseDao

//boiler code
class ReservationListViewModelFactory(
        private val dataSource: ReservationListDatabaseDao,
        private val application: Application) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReservationListViewModel::class.java)) {
                return ReservationListViewModel(dataSource, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}