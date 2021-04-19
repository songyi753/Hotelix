package com.example.hotelix_application.paymentfragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hotelix_application.R
import com.example.hotelix_application.database.payment.PaymentViewModel
import com.example.hotelix_application.databinding.FragmentDisplayPaymentInfoBinding

class DisplayPaymentInfoFragment : Fragment(){

    private val args by navArgs<DisplayPaymentInfoFragmentArgs>()
    private var _binding: FragmentDisplayPaymentInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var mPaymentViewModel: PaymentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDisplayPaymentInfoBinding.inflate(inflater, container, false)

        mPaymentViewModel = ViewModelProvider(this).get(PaymentViewModel::class.java)

        binding.reservationIdView.setText(args.currentPayment.reservationId)
        binding.customerNameView.setText(args.currentPayment.customerName)
        binding.paymentMethodView.setText(args.currentPayment.paymentMethod)
        binding.paidAmountView.setText("RM ${args.currentPayment.paidAmount.toString()}")
        binding.paymentDateView.setText(args.currentPayment.paymentDate)

        binding.viewHistoryButton.setOnClickListener{
            val action = DisplayPaymentInfoFragmentDirections.actionDisplayPaymentInfoFragmentToFragmentPaymentHistory(args.currentPayment)
            findNavController().navigate(action)
        }

        binding.refundButton.setOnClickListener{
            refundPayment()
        }

        //add menu
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_status_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.status_menu_delete){
            deletePayment()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deletePayment(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _, _ ->
            mPaymentViewModel.deletePayment(args.currentPayment)
            Toast.makeText(requireContext(), "Successfully removed ${args.currentPayment.customerName} payment info.", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_displayPaymentInfoFragment_to_searchPaymentFragment)
        }
        builder.setNegativeButton("No"){ _, _ ->

        }
        builder.setTitle("Delete ${args.currentPayment.customerName} payment info?")
        builder.setMessage("Are you sure you want to delete ${args.currentPayment.customerName} payment info?")
        builder.create().show()
    }

    private fun refundPayment(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _, _ ->
            mPaymentViewModel.deletePayment(args.currentPayment)
            Toast.makeText(requireContext(), "Successfully refund ${args.currentPayment.customerName} payment. The payment has been removed.", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_displayPaymentInfoFragment_to_searchPaymentFragment)
        }
        builder.setNegativeButton("No"){ _, _ ->

        }
        builder.setTitle("Refund ${args.currentPayment.customerName} payment?")
        builder.setMessage("Are you sure to refund ${args.currentPayment.customerName} payment?")
        builder.create().show()
    }
}