package com.example.hotelix_application.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelix_application.R
import com.example.hotelix_application.database.payment.Payment
import com.example.hotelix_application.paymentfragment.SearchPaymentFragmentDirections
import kotlinx.android.synthetic.main.list_item_payment_list.view.*
import kotlinx.android.synthetic.main.list_item_payment_list.view.reservation_id_txt
import kotlinx.android.synthetic.main.list_item_payment_list.view.customer_name_txt


class PaymentListAdapter: RecyclerView.Adapter<PaymentListAdapter.MyViewHolder>() {

    private var paymentList = emptyList<Payment>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_payment_list, parent, false))
    }

    override fun getItemCount(): Int {
        return paymentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentPayment = paymentList[position]
        holder.itemView.reservation_id_txt.text = currentPayment.reservationId
        holder.itemView.customer_name_txt.text = currentPayment.customerName

        holder.itemView.row_layout_payment.setOnClickListener{
            val action = SearchPaymentFragmentDirections.actionSearchPaymentFragmentToDisplayPaymentInfoFragment(currentPayment)
            holder.itemView.findNavController().navigate(action)
        }
    }

    fun setData(payment: List<Payment>){
        this.paymentList = payment
        notifyDataSetChanged()
    }
}