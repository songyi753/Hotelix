package com.example.hotelix_application.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelix_application.R
import com.example.hotelix_application.ReservationListFragmentDirections
import com.example.hotelix_application.database.reservation.ReservationList
import com.example.hotelix_application.model.reservation.ReservationListViewModel
import kotlinx.android.synthetic.main.reservation_list_item_view.view.*

class ReservationListAdapter: RecyclerView.Adapter<ReservationListAdapter.ReservationListViewHolder>() {

    var data = listOf<ReservationList>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    /**
     * Creates new views with R.layout.item_view as its template
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationListViewHolder {
        val layout = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.reservation_list_item_view, parent, false)

        return ReservationListViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * Replaces the content of an existing view with new data
     */
    override fun onBindViewHolder(holder: ReservationListViewHolder, position: Int) {
        val item = data[position]
        holder.bindItems(item)

        holder.itemView.rowLayout.setOnClickListener{
            val action = ReservationListFragmentDirections.actionReservationListFragmentToEditReservationFragment(item)
            holder.itemView.findNavController().navigate(action)
        }
    }

    class ReservationListViewHolder(val view: View) : RecyclerView.ViewHolder(view){

        fun bindItems(item: ReservationList){
            view.reserve_id.text = "ID : " + item.generatedID
            view.customer_name.text = "Name : " + item.name
            view.reserve_date.text = "Reservation Date : " + item.reserveDate.toString()
            view.room_type.text = "Room ID : " + item.roomId
            view.contact_number.text = "Contact : " + item.contactNumber
        }

    }

}