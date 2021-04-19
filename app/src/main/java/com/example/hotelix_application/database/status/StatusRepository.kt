package com.example.hotelix_application.database.status

import androidx.lifecycle.LiveData

class StatusRepository(private val statusDao: StatusDao) {
    val readAllData: LiveData<List<Status>> = statusDao.readAllData()

    suspend fun addStatus(status: Status){
        statusDao.addStatus(status)
    }

    suspend fun updateStatus(status: Status){
        statusDao.updateStatus(status)
    }

    suspend fun deleteStatus(status: Status){
        statusDao.deleteStatus(status)
    }

    suspend fun deleteAllStatus(){
        statusDao.deleteAllStatus()
    }

    fun searchStatus(searchQuery: String): LiveData<List<Status>>{
        return statusDao.searchStatus(searchQuery)
    }


}