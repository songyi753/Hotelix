package com.example.hotelix_application.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelix_application.R
import com.example.hotelix_application.database.status.Status
import com.example.hotelix_application.statusfragment.RoomCleaningStatusFragmentDirections
import kotlinx.android.synthetic.main.list_item_tasklist.view.*
import kotlinx.android.synthetic.main.list_item_tasklist.view.room_cleaning_status_button
import kotlinx.android.synthetic.main.list_item_tasklist.view.room_name

class StatusListAdapter: RecyclerView.Adapter<StatusListAdapter.MyViewHolder>() {

    private var statusList = emptyList<Status>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_tasklist, parent, false))
    }

    override fun getItemCount(): Int {
        return statusList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = statusList[position]
        //val res = holder.itemView.context.resources
        holder.itemView.room_id.text = currentItem.roomId.toString()
        holder.itemView.room_name.text = currentItem.roomName
        holder.itemView.room_cleaning_status_button.setBackgroundColor(
            when (currentItem.roomCleaningStatus) {
                -1 -> Color.parseColor("#FB6B6B")
                0 -> Color.parseColor("#F9C912")
                else -> Color.parseColor("#7CDB6D")
            }
        )

        //when a user click on an item in recycler view, it redirects to the update fragment
        holder.itemView.row_layout.setOnClickListener {
            val action = RoomCleaningStatusFragmentDirections.actionRoomCleaningStatusFragmentToUpdateStatusFragment(currentItem) //pass in current item
            holder.itemView.findNavController().navigate(action)
        }
    }

    fun setData(status: List<Status>){
        this.statusList = status
        notifyDataSetChanged()
    }
}