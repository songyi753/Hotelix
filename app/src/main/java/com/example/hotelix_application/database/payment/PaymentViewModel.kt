package com.example.hotelix_application.database.payment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope

import com.example.hotelix_application.database.HotelixDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaymentViewModel(application: Application): AndroidViewModel(application) {

    val readAllPayment: LiveData<List<Payment>>
    private val repository: PaymentRepository

    init{
        val paymentDao = HotelixDatabase.getInstance(application).paymentDao
        repository = PaymentRepository(paymentDao)
        readAllPayment = repository.readAllPayment
    }

    fun addPayment(payment: Payment){
        viewModelScope.launch (Dispatchers.IO){
            repository.addPayment(payment)
        }
    }

    fun deletePayment(payment: Payment){
        viewModelScope.launch (Dispatchers.IO){
            repository.deletePayment(payment)
        }
    }

    fun deleteAllPayment(){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteAllPayments()
        }
    }

    fun searchPayment(searchQuery: String): LiveData<List<Payment>>{
        return repository.searchPayment(searchQuery)
    }

    fun readHistoryFromOneCustomer(searchQuery: String): LiveData<List<Payment>>{
        return repository.readHistoryFromOneCustomer(searchQuery)
    }
}