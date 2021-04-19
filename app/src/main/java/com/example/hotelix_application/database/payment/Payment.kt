package com.example.hotelix_application.database.payment

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity (tableName = "payment_table")
data class Payment (
    @PrimaryKey(autoGenerate = true)
    var paymentId: Long = 0L,

    @ColumnInfo(name = "reservation_id")
    var reservationId: String,

    @ColumnInfo(name = "customer_name")
    var customerName: String,

    @ColumnInfo(name = "payment_method")
    var paymentMethod: String,

    @ColumnInfo(name = "paid_amount")
    var paidAmount: Int,

    @ColumnInfo(name = "payment_date")
    var paymentDate: String,
): Parcelable