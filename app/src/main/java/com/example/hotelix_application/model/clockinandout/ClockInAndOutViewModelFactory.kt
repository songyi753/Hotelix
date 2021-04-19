package com.example.hotelix_application.model.clockinandout

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hotelix_application.database.clockInAndOut.ClockInAndOutDatabaseDao

class ClockInAndOutViewModelFactory(
    private val ClockKey: ClockInAndOutDatabaseDao,
    private val dataSource: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClockInAndOutViewModel::class.java)) {
            return ClockInAndOutViewModel(ClockKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}