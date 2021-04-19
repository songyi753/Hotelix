package com.example.hotelix_application.statusfragment


import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelix_application.R
import com.example.hotelix_application.adapters.StatusListAdapter
import com.example.hotelix_application.database.status.StatusViewModel
import com.example.hotelix_application.databinding.FragmentRoomCleaningStatusBinding


class RoomCleaningStatusFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var mStatusViewModel: StatusViewModel
    private val adapter: StatusListAdapter by lazy { StatusListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentRoomCleaningStatusBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_room_cleaning_status, container, false
        )

        //recrycler view
//        val adapter = StatusListAdapter()
        val recyclerView = binding.statusListRecyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //StatusViewModel
        mStatusViewModel = ViewModelProvider(this).get(StatusViewModel::class.java)
        mStatusViewModel.readAllData.observe(viewLifecycleOwner, Observer { status ->
            adapter.setData(status)
        })

//        binding.viewTaskList.setOnClickListener{
//            findNavController().navigate(R.id.action_roomCleaningStatusFragment_to_taskListFragment)
//        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_roomCleaningStatusFragment_to_addStatusFragment)
        }

        //Add menu
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_status_menu, menu)

        val search = menu?.findItem(R.id.status_menu_search)
        val searchView = search?.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.status_menu_delete) {
            deleteAllUser()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllUser() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mStatusViewModel.deleteAllStatus()
            Toast.makeText(
                requireContext(),
                "Successfully removed all the status. ",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ ->

        }
        builder.setTitle("Delete everything?")
        builder.setMessage("Are you sure you want to delete all the status?")
        builder.create().show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null){
            searchStatus(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query != null){
            searchStatus(query)
        }
        return true
    }

    private fun searchStatus(query: String) {
        val searchQuery = "%$query%"
//        val adapter = StatusListAdapter()
        mStatusViewModel.searchStatus(searchQuery).observe(this) { task ->
            task.let {
                adapter.setData(it)
            }
        }
    }

}