package com.example.hotelix_application.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelix_application.R
import com.example.hotelix_application.database.staff.StaffList
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.list_item_staff_list.view.*
import java.util.*


class StaffAdapter(options: FirestoreRecyclerOptions<StaffList>) : FirestoreRecyclerAdapter<StaffList, StaffAdapter.StaffViewHolder>(
    options
) {

    class StaffViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val staffId = itemView.textView4
        val staffName = itemView.textView5
        val position = itemView.textView6
        val email = itemView.textView7
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item_staff_list, parent, false)

        return StaffViewHolder(layout)
    }

    override fun onBindViewHolder(holder: StaffViewHolder, position: Int, model: StaffList) {

        holder.staffId.text = "Staff ID :"+ model.userId
        holder.staffName.text = "Staff Name :"+model.name
        holder.position.text = "Staff Position :"+model.position
        holder.email.text = "Staff Email :"+model.userEmail

    }


}