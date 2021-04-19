package com.example.hotelix_application.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelix_application.R
import com.example.hotelix_application.database.payment.Payment
import kotlinx.android.synthetic.main.list_item_payment_history_list.view.*

class PaymentHistoryListAdapter: RecyclerView.Adapter<PaymentHistoryListAdapter.MyViewHolder>() {

    private var paymentHistoryList = emptyList<Payment>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_payment_history_list, parent, false))
    }

    override fun getItemCount(): Int {
        return paymentHistoryList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentPaymentHistory = paymentHistoryList[position]
        holder.itemView.reservation_date_txt.text = currentPaymentHistory.paymentDate
        holder.itemView.paid_amount_txt.text = currentPaymentHistory.paidAmount.toString()
    }

    fun setData(payment: List<Payment>){
        this.paymentHistoryList = payment
        notifyDataSetChanged()
    }
}