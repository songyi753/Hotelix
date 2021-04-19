package com.example.hotelix_application.database.status

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.hotelix_application.database.HotelixDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StatusViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Status>>
    private val repository: StatusRepository


    init{ //execute first
        val statusDao = HotelixDatabase.getInstance(application).statusDao
        repository = StatusRepository(statusDao)
        readAllData = repository.readAllData
    }

    fun addStatus(status:Status){
        viewModelScope.launch(Dispatchers.IO) { // run this code in background
            repository.addStatus(status)
        }
    }

    fun updateStatus(status: Status){
        viewModelScope.launch (Dispatchers.IO){
            repository.updateStatus(status)
        }
    }

    fun deleteStatus(status: Status){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteStatus(status)
        }
    }

    fun deleteAllStatus(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllStatus()
        }
    }

    fun searchStatus(searchQuery: String): LiveData<List<Status>>{
        return repository.searchStatus(searchQuery)
    }
}