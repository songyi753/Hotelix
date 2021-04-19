package com.example.hotelix_application


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavAction
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelix_application.adapters.RoomsListAdapter
import com.example.hotelix_application.database.HotelixDatabase
import com.example.hotelix_application.database.Rooms.RoomExtra
import com.example.hotelix_application.databinding.FragmentRoomBinding
import com.example.hotelix_application.model.rooms.RoomsListViewModel
import com.example.hotelix_application.model.rooms.RoomsListViewModelFactory
import com.google.common.base.Ascii.toLowerCase
import kotlinx.android.synthetic.main.fragment_reservation_list.*
import kotlinx.android.synthetic.main.fragment_room.*
import kotlinx.android.synthetic.main.fragment_room.title


class RoomFragment : Fragment(), SearchView.OnQueryTextListener{

    private var binding: FragmentRoomBinding? = null
    private val _binding get() = binding!! //ensure pass value (for recycler view)
    private lateinit var recyclerView: RecyclerView //to bind recycler view
    private lateinit var sharedViewModel: RoomsListViewModel
    private var isReservation = false
    private val adapter: RoomsListAdapter by lazy { RoomsListAdapter() }
    private lateinit var action:NavDirections
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val tv = view?.findViewById<TextView>(R.id.title)
//        if (tv != null) {
//            tv.text = arguments?.getString("rType")
//        }
//
//
//
//    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
): View? {

        val fragmentBinding = FragmentRoomBinding.inflate(inflater, container, false)
        fragmentBinding.addRoomBtn.visibility=View.VISIBLE
        binding = fragmentBinding

        val application = requireNotNull(this.activity).application

        //create instance of ViewModel Factory
        val dataSource = HotelixDatabase.getInstance(application).roomsListDao
        val viewModelFactory = RoomsListViewModelFactory(dataSource, application)

        val tv = binding?.title

        if (tv != null) {
            tv.text = arguments?.getString("rType")
        }

        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        if(arguments?.getBoolean("isReservation") == true){
            isReservation = true

        }

        //get reference to ViewModel associated with fragment
        sharedViewModel =
                ViewModelProvider(
                        this, viewModelFactory
                ).get(RoomsListViewModel::class.java)

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = _binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
//        val adapter = RoomsListAdapter()


        binding?.apply {
            recyclerView.adapter = adapter
            lifecycleOwner = viewLifecycleOwner
            roomsListViewModel = sharedViewModel
            roomFragment = this@RoomFragment //bind fragment in xml
        }

        if(isReservation){
            add_room_btn.visibility=View.INVISIBLE
        }
        else{
            add_room_btn.visibility=View.VISIBLE
        }

        if(isReservation){
            when {
                toLowerCase(title.text).equals("single room") -> {
                    sharedViewModel.availableSingleRoomsList.observe(viewLifecycleOwner, Observer {
                        it?.let {
                            adapter.data = it
                            if(isReservation){
                                adapter.isReservation = true
                            }
                        }
//                        val count = (adapter.itemCount + 1).toString()
//                        var bundle = bundleOf("count" to count)
                    })

                }
                toLowerCase(title.text).equals("vip room") -> {
                    sharedViewModel.availableVipRoomsList.observe(viewLifecycleOwner, Observer {
                        it?.let {
                            adapter.data = it
                            if(isReservation){
                                adapter.isReservation = true
                            }
                        }
                    })
                }
                else -> {
                    sharedViewModel.availablePresidentRoomsList.observe(viewLifecycleOwner, Observer {
                        it?.let {
                            adapter.data = it
                            if(isReservation){
                                adapter.isReservation = true
                            }
                        }
                    })
                }
            }
        }
        else{
            when{
                title.text.equals("Single Room") -> {
                    sharedViewModel.singleRoomsList.observe(viewLifecycleOwner, Observer {
                        it?.let {
                            adapter.data = it
                            if(isReservation){
                                adapter.isReservation = true
                            }
                        }
                        val count = RoomExtra(adapter.itemCount,"single")
                        action = RoomFragmentDirections.actionRoomFragmentToAddRoomFragment(count)
                    })



                    searchView1.isSubmitButtonEnabled =true
                    searchView1.setOnQueryTextListener(this)
                }
                title.text.equals("Vip Room") -> {
                    sharedViewModel.vipRoomsList.observe(viewLifecycleOwner, Observer {
                        it?.let {
                            adapter.data = it
                            if(isReservation){
                                adapter.isReservation = true
                            }
                        }
                        val count = RoomExtra(adapter.itemCount,"vip")
                        action = RoomFragmentDirections.actionRoomFragmentToAddRoomFragment(count)
                    })

                }
                else -> {
                    sharedViewModel.presidentRoomsList.observe(viewLifecycleOwner, Observer {
                        it?.let {
                            adapter.data = it
                            if(isReservation){
                                adapter.isReservation = true
                            }
                        }
                        val count = RoomExtra(adapter.itemCount,"president")
                        action = RoomFragmentDirections.actionRoomFragmentToAddRoomFragment(count)
                    })

                }
            }

        }
        searchView1.isSubmitButtonEnabled =true
        searchView1.setOnQueryTextListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun navigate(){

        var bundle = bundleOf("count" to adapter.itemCount)
        findNavController().navigate(action)

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null){
            searchRoom(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query != null){
            searchRoom(query)
        }
        return true
    }

    private fun searchRoom(query: String){
        val searchQuery = "%$query%"

        sharedViewModel.searchRoom(searchQuery).observe(this){list ->
            list.let {
                adapter.data = it
            }
        }
    }
}


