package com.example.hotelix_application.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelix_application.CheckInFragment
import com.example.hotelix_application.CheckInFragmentDirections
import com.example.hotelix_application.R
import com.example.hotelix_application.RoomFragmentDirections
import com.example.hotelix_application.database.Rooms.RoomsList
import com.example.hotelix_application.model.rooms.RoomsListViewModel
import com.example.hotelix_application.paymentfragment.SearchPaymentFragmentDirections
import kotlinx.android.synthetic.main.fragment_check_in.view.*
import kotlinx.android.synthetic.main.list_item_payment_list.view.*
import kotlinx.android.synthetic.main.rooms_list_item_view.view.*

class CheckInListAdapter: RecyclerView.Adapter<CheckInListAdapter.RoomsListViewHolder>()  {

    var data = listOf<RoomsList>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    /**
     * Creates new views with R.layout.item_view as its template
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckInListAdapter.RoomsListViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.rooms_list_item_view, parent, false)

        return RoomsListViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * Replaces the content of an existing view with new data
     */
    override fun onBindViewHolder(holder: RoomsListViewHolder, position: Int) {
        val item = data[position]
        holder.bindItems(item)

//        holder.itemView.check_in_recycle_view.setOnClickListener{
//            val action = RoomFragmentDirections.actionRoomFragmentToEditRoomFragment(item)
//        }
        holder.itemView.roomsList.setOnClickListener{

                val action = CheckInFragmentDirections.actionCheckInFragmentToUpdateCheckInStatusFragment(item)
                holder.itemView.findNavController().navigate(action)


        }
    }

    class RoomsListViewHolder(val view: View) : RecyclerView.ViewHolder(view){

        fun bindItems(item: RoomsList){

            view.reserve_id.text = "Room ID   :"+item.generatedRoomID
            view.type.text ="Room Type:"+ item.roomType
            view.status.text = "Status    :"+item.status


        }
//        init{
//            view.setOnClickListener{
//                //pass current item
//
//            }
//        }

    }

}