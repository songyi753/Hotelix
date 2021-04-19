package com.example.hotelix_application

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelix_application.adapters.ReservationListAdapter
import com.example.hotelix_application.database.HotelixDatabase
import com.example.hotelix_application.database.reservation.ReservationList
import com.example.hotelix_application.databinding.FragmentReservationListBinding
import com.example.hotelix_application.model.reservation.ReservationListViewModel
import com.example.hotelix_application.model.reservation.ReservationListViewModelFactory
import kotlinx.android.synthetic.main.fragment_reservation_list.*
import java.util.*


class ReservationListFragment : Fragment(), SearchView.OnQueryTextListener {

    private var binding: FragmentReservationListBinding? = null
    private val _binding get() = binding!! //ensure pass value (for recycler view)
    private lateinit var recyclerView: RecyclerView //to bind recycler view
    private lateinit var sharedViewModel: ReservationListViewModel
    private val adapter: ReservationListAdapter by lazy { ReservationListAdapter() }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentReservationListBinding.inflate(inflater, container, false)
        binding = fragmentBinding

        val application = requireNotNull(this.activity).application

        //create instance of ViewModel Factory
        val dataSource = HotelixDatabase.getInstance(application).reservationListDao
        val viewModelFactory = ReservationListViewModelFactory(dataSource, application)

        //get reference to ViewModel associated with fragment
        sharedViewModel =
                ViewModelProvider(
                        this,viewModelFactory).get(ReservationListViewModel::class.java)


        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recyclerView = _binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        //val adapter = ReservationListAdapter()

        binding?.apply {
            recyclerView.adapter = adapter
            lifecycleOwner = viewLifecycleOwner
            reservationListViewModel = sharedViewModel
            reservationListFragment = this@ReservationListFragment
        }

        sharedViewModel.reservationList.observe(viewLifecycleOwner, Observer{
            it?.let{
                adapter.data = it
            }
        })

        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun navigate(){
        findNavController().navigate(R.id.action_reservationListFragment_to_addReservationFragment)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null){
            searchReservation(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
       if(query != null){
            searchReservation(query)
       }
        return true
    }

    private fun searchReservation(query: String){
        val searchQuery = "%$query%"

        sharedViewModel.searchReservation(searchQuery).observe(this){list ->
            list.let {
                adapter.data = it
            }
        }
    }
}