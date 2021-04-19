package com.example.hotelix_application.model.clockinandout

import android.app.Application
import androidx.lifecycle.*
import com.example.hotelix_application.database.clockInAndOut.ClockInAndOut
import com.example.hotelix_application.database.clockInAndOut.ClockInAndOutDatabaseDao
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ClockInAndOutViewModel(
    dataSource: ClockInAndOutDatabaseDao,
    application: Application
) : ViewModel(){

    private val _ggcom = MutableLiveData<Boolean>(false)
    val ggcom : LiveData<Boolean?>
        get() = _ggcom

    val database1 = dataSource
    val clockList = database1.getAllClocks(FirebaseAuth.getInstance().currentUser.uid)

    private suspend fun insert(Clock: ClockInAndOut){
        database1.insert(Clock)
    }

    private suspend fun update(Clock: ClockInAndOut){
        database1.update(Clock)
    }

    private suspend fun getClockFromDatabase(): ClockInAndOut? {
        var clock = database1.getClock()
        if (clock?.clockEndTimeMilli != clock?.clockStartTimeMilli) {
            clock = null
        }
        return clock
    }

    fun onStartTracking() {
        viewModelScope.launch {
            val newClock = ClockInAndOut()
            newClock.hasClockOut = true
            insert(newClock)
        }
    }

    fun onStopTracking() {
        viewModelScope.launch {
            val oldClock = database1.getClocks(database1.getLatestClockId())
            oldClock.clockEndTimeMilli = System.currentTimeMillis()
            oldClock.hasClockOut = false
            update(oldClock)
        }
    }

    fun compare(){
        viewModelScope.launch {
            if(database1 != null && database1.getLatestClockId() != null) {
                val oldClock = database1.getClocks(database1.getLatestClockId())
                if (oldClock != null && oldClock.hasClockOut == true) {
                    setggcomT()
                } else {
                    setggcomF()
                }
            }
        }
    }

    fun setggcomT(){
        _ggcom.value=true
    }

    fun setggcomF(){
        _ggcom.value=false
    }



}