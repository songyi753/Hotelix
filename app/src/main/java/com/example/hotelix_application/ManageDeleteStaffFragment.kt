package com.example.hotelix_application

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelix_application.adapters.ClockAdapter
import com.example.hotelix_application.adapters.StaffAdapter
import com.example.hotelix_application.database.staff.StaffList
import com.example.hotelix_application.databinding.FragmentManageDeleteStaffBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_manage_delete_staff.*

class ManageDeleteStaffFragment : Fragment() {
    private var binding: FragmentManageDeleteStaffBinding? = null
    private val fStore : FirebaseFirestore = FirebaseFirestore.getInstance()
    private val query = fStore.collection("Users")

    var staffAdapter : StaffAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentManageDeleteStaffBinding.inflate(inflater, container, false)
        binding = fragmentBinding

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        floatingActionButton_add_staff.setOnClickListener(){
            findNavController().navigate(R.id.action_manageDeleteStaffFragment_to_addStaffFragment)
        }

        val staffQuery:Query = query
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<StaffList> = FirestoreRecyclerOptions.Builder<StaffList>()
            .setQuery(staffQuery, StaffList::class.java)
            .build()

        staffAdapter = StaffAdapter(firestoreRecyclerOptions)

        firestore_list.layoutManager = LinearLayoutManager(context)
        firestore_list.adapter = staffAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onStart(){
        super.onStart()
        staffAdapter?.startListening()
    }

    override fun onDestroy(){
        super.onDestroy()
        staffAdapter?.stopListening()
    }

}