package com.example.hotelix_application

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelix_application.adapters.CheckInListAdapter
import com.example.hotelix_application.adapters.RoomsListAdapter
import com.example.hotelix_application.database.HotelixDatabase
import com.example.hotelix_application.databinding.FragmentCheckInBinding

import com.example.hotelix_application.model.rooms.RoomsListViewModel
import com.example.hotelix_application.model.rooms.RoomsListViewModelFactory
import kotlinx.android.synthetic.main.fragment_room.*


class CheckInFragment : Fragment() {
    private var binding: FragmentCheckInBinding? = null
    private val _binding get() = binding!! //ensure pass value (for recycler view)
    private lateinit var recyclerView: RecyclerView //to bind recycler view
    private lateinit var sharedViewModel: RoomsListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentCheckInBinding.inflate(inflater, container, false)
        binding = fragmentBinding

        val application = requireNotNull(this.activity).application

        //create instance of ViewModel Factory
        val dataSource = HotelixDatabase.getInstance(application).roomsListDao
        val viewModelFactory = RoomsListViewModelFactory(dataSource, application)

        val tv = binding?.title

        if (tv != null) {
            tv.text = arguments?.getString("status")
        }

        //get reference to ViewModel associated with fragment
        sharedViewModel =
            ViewModelProvider(
                this,viewModelFactory).get(RoomsListViewModel::class.java)

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recyclerView = _binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = CheckInListAdapter()

        binding?.apply {
            recyclerView.adapter = adapter
            lifecycleOwner = viewLifecycleOwner
            roomsListViewModel = sharedViewModel
            checkInFragment = this@CheckInFragment
        }

        when{
            title.text.equals("Pending") -> {
                sharedViewModel.pendingRoomList.observe(viewLifecycleOwner, Observer {
                    it?.let {
                        adapter.data = it
                    }
                })

            }
            else -> {
                sharedViewModel.checkedInRoomList.observe(viewLifecycleOwner, Observer {
                    it?.let {
                        adapter.data = it
                    }
                })
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun navigate(){
        findNavController().navigate(R.id.action_reservationListFragment_to_addReservationFragment)
    }
}