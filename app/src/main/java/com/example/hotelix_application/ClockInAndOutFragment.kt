package com.example.hotelix_application

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelix_application.adapters.ClockAdapter
import com.example.hotelix_application.database.HotelixDatabase
import com.example.hotelix_application.databinding.FragmentClockInAndOutBinding
import com.example.hotelix_application.model.clockinandout.ClockInAndOutViewModel
import com.example.hotelix_application.model.clockinandout.ClockInAndOutViewModelFactory


class ClockInAndOutFragment : Fragment() {

    private var binding: FragmentClockInAndOutBinding? = null
    private val _binding get() = binding!!
    private lateinit var recyclerView1: RecyclerView //to bind recycler view
    private lateinit var sharedViewModel: ClockInAndOutViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentClockInAndOutBinding.inflate(inflater, container, false)
        binding = fragmentBinding

        val application = requireNotNull(this.activity).application
        val dataSource1 = HotelixDatabase.getInstance(application).clockDao
        val viewModelFactory = ClockInAndOutViewModelFactory(dataSource1, application)

        //get reference to ViewModel associated with fragment
        sharedViewModel =
                ViewModelProvider(
                        this, viewModelFactory).get(ClockInAndOutViewModel::class.java)

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ClockAdapter()
        recyclerView1 = _binding.clockRecyclerView
        recyclerView1.layoutManager = LinearLayoutManager(context)


        sharedViewModel.ggcom.observe(viewLifecycleOwner, Observer {
            if(it == true){
                ClockOutVisible()
            }else{
                ClockInVisible()
            }
        })

        binding?.apply {
            recyclerView1.adapter = adapter
            lifecycleOwner = viewLifecycleOwner

        }

        sharedViewModel.clockList.observe(viewLifecycleOwner, Observer{
            it?.let{
                adapter.data = it
            }
        })

        sharedViewModel.compare()
        binding?.ClockIn?.setOnClickListener {
            sharedViewModel.onStartTracking()
            sharedViewModel.compare()
            Toast.makeText(getActivity(),"Successfully Clock In", Toast.LENGTH_LONG).show()

        }

        binding?.ClockOut?.setOnClickListener {
            sharedViewModel.onStopTracking()
            sharedViewModel.compare()
            Toast.makeText(getActivity(),"Successfully Clock Out", Toast.LENGTH_LONG).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun ClockInVisible(){
        binding?.ClockIn?.setVisibility(VISIBLE)
        binding?.ClockOut?.setVisibility(INVISIBLE)
    }

    fun ClockOutVisible(){
        binding?.ClockIn?.setVisibility(INVISIBLE)
        binding?.ClockOut?.setVisibility(VISIBLE)
    }


}