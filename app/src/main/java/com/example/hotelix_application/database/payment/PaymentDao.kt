package com.example.hotelix_application.database.payment

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PaymentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPayment(payment: Payment)

    @Query ("SELECT * FROM payment_table ORDER BY paymentId ASC")
    fun readAllPayments(): LiveData<List<Payment>>

    @Delete
    suspend fun deletePayment(payment: Payment)

    @Query("DELETE FROM payment_table")
    suspend fun deleteAllPayments()

    @Query("SELECT * FROM payment_table WHERE reservation_id LIKE :searchQuery OR customer_name LIKE :searchQuery")
    fun searchPayment(searchQuery: String): LiveData<List<Payment>>

    @Query("SELECT * FROM payment_table WHERE reservation_id = :searchQuery ORDER BY payment_date DESC")
    fun readHistoryFromOneCustomer(searchQuery: String): LiveData<List<Payment>>
}