package com.example.hotelix_application.database.payment

import android.app.DownloadManager
import androidx.lifecycle.LiveData

class PaymentRepository(private val paymentDao: PaymentDao) {

    val readAllPayment: LiveData<List<Payment>> = paymentDao.readAllPayments()

    suspend fun addPayment(payment: Payment){
        paymentDao.addPayment(payment)
    }

    suspend fun deletePayment(payment: Payment){
        paymentDao.deletePayment(payment)
    }

    suspend fun deleteAllPayments(){
        paymentDao.deleteAllPayments()
    }

    fun searchPayment(searchQuery: String): LiveData<List<Payment>>{
        return paymentDao.searchPayment(searchQuery)
    }

    fun readHistoryFromOneCustomer(searchQuery: String): LiveData<List<Payment>>{
        return paymentDao.readHistoryFromOneCustomer(searchQuery)
    }
}