package com.example.hotelix_application.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelix_application.R
import com.example.hotelix_application.database.clockInAndOut.ClockInAndOut
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.clock_list_item_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class ClockAdapter: RecyclerView.Adapter<ClockAdapter.ClockViewHolder>() {

    private lateinit var fStore : FirebaseFirestore
    private lateinit var mAuth : FirebaseAuth

    var data = listOf<ClockInAndOut>()
        set(value){
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClockAdapter.ClockViewHolder {
        val layout = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.clock_list_item_view, parent, false)

        return ClockViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ClockViewHolder, position: Int) {
        val item = data[position]
        fStore = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()

        val uid = mAuth.currentUser.uid

        if(uid == item.staffClockID) {
            //set clock in and out word
            val date1 = toDateStr(item.clockStartTimeMilli, "dd/MM/yy hh:mm:ss")
            val date2 = toDateStr(item.clockEndTimeMilli, "dd/MM/yy hh:mm:ss")
            holder.itemView.clocktitle2.text = "Clock Out At :"
            holder.itemView.clocktitle1.text = "Clock In At :"
            if (item.clockStartTimeMilli >= item.clockEndTimeMilli) {

                holder.itemView.startTime.text = date1
                holder.itemView.endTime.text = "---"
            } else {
                holder.itemView.endTime.text = date2
                holder.itemView.startTime.text = date1
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ClockViewHolder(val view: View) : RecyclerView.ViewHolder(view){

    }

    fun toDateStr(milliseconds: Long, format: String?): String? {
        val date = Date(milliseconds)
        val formatter = SimpleDateFormat(format)
        return formatter.format(date)
    }

}