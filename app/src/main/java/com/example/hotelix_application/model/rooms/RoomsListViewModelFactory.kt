package com.example.hotelix_application.model.rooms
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hotelix_application.database.Rooms.RoomsListDatabaseDao

class RoomsListViewModelFactory(
    private val dataSource: RoomsListDatabaseDao,
    private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomsListViewModel::class.java)) {
            return RoomsListViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}