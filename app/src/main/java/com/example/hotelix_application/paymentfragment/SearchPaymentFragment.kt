package com.example.hotelix_application.paymentfragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelix_application.R
import com.example.hotelix_application.adapters.PaymentListAdapter
import com.example.hotelix_application.database.payment.PaymentViewModel
import com.example.hotelix_application.databinding.FragmentSearchPaymentBinding

class SearchPaymentFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentSearchPaymentBinding? = null
    private val binding get() = _binding!!
    private lateinit var mPaymentViewModel: PaymentViewModel
    private val adapter: PaymentListAdapter by lazy { PaymentListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchPaymentBinding.inflate(inflater, container, false)

        //recycler view
//       val adapter = PaymentListAdapter()
        val recyclerView = binding.paymentRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //paymentviewmodel
        mPaymentViewModel = ViewModelProvider(this).get(PaymentViewModel::class.java)
        mPaymentViewModel.readAllPayment.observe(viewLifecycleOwner, Observer { payment ->
            adapter.setData(payment)
        })

        binding.floatingActionButtonAddPayment.setOnClickListener{
            findNavController().navigate(R.id.action_searchPaymentFragment_to_addPaymentFragment)
        }

        //add menu
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
        if(item.itemId == R.id.status_menu_delete){
            deleteAllPayments()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllPayments(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _, _ ->
            mPaymentViewModel.deleteAllPayment()
            Toast.makeText(requireContext(), "Successfully removed all payments info.", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){ _, _ ->

        }
        builder.setTitle("Delete all payment info?")
        builder.setMessage("Are you sure you want to delete all payment info?")
        builder.create().show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null){
            searchPayment(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query != null){
            searchPayment(query)
        }
        return true
    }

    private fun searchPayment(query: String){
        val searchQuery = "%$query%"
        mPaymentViewModel.searchPayment(searchQuery).observe(this) { list ->
            list.let {
                adapter.setData(it)
            }
        }
    }
}