package com.example.hotelix_application.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.hotelix_application.R
import com.example.hotelix_application.RoomFragmentDirections
import com.example.hotelix_application.database.Rooms.RoomListWithReservation
import com.example.hotelix_application.database.Rooms.RoomsList
import kotlinx.android.synthetic.main.rooms_list_item_view.view.*

class RoomsListAdapter: RecyclerView.Adapter<RoomsListAdapter.RoomsListViewHolder>() {


    var data = listOf<RoomsList>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    var isReservation = false

    /**
     * Creates new views with R.layout.item_view as its template
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomsListAdapter.RoomsListViewHolder {
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

        holder.itemView.roomsList.setOnClickListener{
            if(isReservation){
                val room = RoomListWithReservation(item.roomID, item.generatedRoomID, item.roomType, item.status, true)
                val action = RoomFragmentDirections.actionRoomFragmentToEditRoomFragment(room)
                holder.itemView.findNavController().navigate(action)
            }
            else{
                val room = RoomListWithReservation(item.roomID, item.generatedRoomID, item.roomType, item.status, false)
                val action = RoomFragmentDirections.actionRoomFragmentToEditRoomFragment(room)
                holder.itemView.findNavController().navigate(action)
            }
        }
    }

    class RoomsListViewHolder(val view: View) : RecyclerView.ViewHolder(view){

        fun bindItems(item: RoomsList){

            view.reserve_id.text = "Room ID   :"+item.generatedRoomID
            view.type.text = "Room Type :"+item.roomType
            view.status.text ="Status    :"+ item.status


        }
//        init{
//            view.setOnClickListener{
//                //pass current item
//                view.findNavController().navigate(R.id.action_roomFragment_to_editRoomFragment)
//            }
//        }
    }
//    private fun updateRoomStatus(room: RoomsList){
//
//    }

}