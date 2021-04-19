package com.example.hotelix_application.paymentfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelix_application.R
import com.example.hotelix_application.adapters.PaymentHistoryListAdapter
import com.example.hotelix_application.adapters.PaymentListAdapter
import com.example.hotelix_application.database.payment.PaymentViewModel
import com.example.hotelix_application.databinding.FragmentDisplayPaymentInfoBinding
import com.example.hotelix_application.databinding.FragmentPaymentHistoryBinding


class FragmentPaymentHistory : Fragment() {
    private val args by navArgs<FragmentPaymentHistoryArgs>()
    private var _binding: FragmentPaymentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var mPaymentViewModel: PaymentViewModel
    private val adapter: PaymentHistoryListAdapter by lazy { PaymentHistoryListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPaymentHistoryBinding.inflate(inflater, container, false)

        //recycler view
        val recyclerView = binding.paymentHistoryRecyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.customerNameTitle.setText(args.currentPayment.customerName)

        //paymentviewmodel
        mPaymentViewModel = ViewModelProvider(this).get(PaymentViewModel::class.java)
        mPaymentViewModel.readHistoryFromOneCustomer(args.currentPayment.reservationId).observe(viewLifecycleOwner, Observer { payment ->
            adapter.setData(payment)
        })

        return binding.root
    }

}