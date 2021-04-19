package com.example.hotelix_application.paymentfragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hotelix_application.R
import com.example.hotelix_application.database.payment.Payment
import com.example.hotelix_application.database.payment.PaymentViewModel
import com.example.hotelix_application.databinding.FragmentAddPaymentBinding
import com.example.hotelix_application.databinding.FragmentSearchPaymentBinding
import kotlinx.android.synthetic.main.fragment_add_payment.*
import java.util.*

class AddPaymentFragment : Fragment() {

    private var _binding: FragmentAddPaymentBinding? = null
    private val binding get() = _binding!!
    private lateinit var mPaymentViewModel: PaymentViewModel
    private val args by navArgs<AddPaymentFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddPaymentBinding.inflate(inflater, container, false)

        mPaymentViewModel = ViewModelProvider(this).get(PaymentViewModel::class.java)

        //set text from reservation args
        binding.addReservationIdTextboxLayout.editText?.setText(args.currentReservation?.generatedID)
        binding.addCustomerNameTextboxLayout.editText?.setText(args.currentReservation?.name)
        binding.addPaymentDateTextboxLayout.editText?.setText(args.currentReservation?.reserveDate)
        binding.addPaidAmountTextboxLayout.editText?.setText(checkPrice())

        binding.addPaymentButton.setOnClickListener{
            insertPaymentToDatabase()
        }

        binding.addPaymentDateTextboxEdittext.setOnClickListener{
            datePicker()
        }
        return binding.root
    }

    override fun onResume() {
        val status = resources.getStringArray(R.array.payment_method_array)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.status_dropdown, status)
        binding.addPaymentMethodTextboxLayoutAutoCompleteTextView?.setAdapter(arrayAdapter)
        super.onResume()
    }

    private fun insertPaymentToDatabase(){
        val reservationId = binding.addReservationIdTextboxEdittext.text.toString()
        val customerName = binding.addCustomerNameTextboxEdittext.text.toString()
        val paymentMethod = binding.addPaymentMethodTextboxLayoutAutoCompleteTextView.text.toString()
        val paidAmount = binding.addPaidAmountTextboxEdittext.text.toString()
        val paymentDate = binding.addPaymentDateTextboxEdittext.text.toString()

//        if(inputCheck(reservationId, customerName, paymentMethod, paidAmount, paymentDate)){
//            //create payment object
//            val payment = Payment(0, reservationId, customerName, paymentMethod, Integer.parseInt(paidAmount), paymentDate)
//            //add payment to database
//            mPaymentViewModel.addPayment(payment)
//            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
//            //navigate back
//            findNavController().navigate(R.id.action_addPaymentFragment_to_searchPaymentFragment)
//        }else{
//            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show()
//        }

        if(TextUtils.isEmpty(reservationId)){
            Toast.makeText(requireContext(), "Please fill out Reservation Id field.", Toast.LENGTH_SHORT).show()
        }else if(TextUtils.isEmpty(customerName)){
            Toast.makeText(requireContext(), "Please fill out Customer Name field.", Toast.LENGTH_SHORT).show()
        }else if(TextUtils.isEmpty(paidAmount)){
            Toast.makeText(requireContext(), "Please fill out Paid Amount field.", Toast.LENGTH_SHORT).show()
        }else if(TextUtils.isEmpty(paymentDate)){
            Toast.makeText(requireContext(), "Please fill out Payment Date field.", Toast.LENGTH_SHORT).show()
        }else{
            //create payment object
            val payment = Payment(0, reservationId, customerName, paymentMethod, Integer.parseInt(paidAmount), paymentDate)
            //add payment to database
            mPaymentViewModel.addPayment(payment)
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
            //navigate back
            findNavController().navigate(R.id.action_addPaymentFragment_to_searchPaymentFragment)
        }
    }

    private fun inputCheck(reservationId:String, customerName:String, paymentMethod:String, paidAmount:String, paymentDate:String):Boolean {
        return !(TextUtils.isEmpty(reservationId) && TextUtils.isEmpty(customerName) && TextUtils.isEmpty(paymentMethod) && TextUtils.isEmpty(paidAmount) && TextUtils.isEmpty(paymentDate))
    }

    private fun datePicker(){
        val cal: Calendar = Calendar.getInstance()
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)

        val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener{view, mYear, mMonth, mDay ->
            binding.addPaymentDateTextboxLayout.editText?.setText("${mDay}/${mMonth}/${mYear}")
        }, year, month, day)

        dpd.show()
    }

    private fun checkPrice(): String{
        var price: String = ""
        if(args.currentReservation != null){
            if(args.currentReservation?.roomId!!.startsWith("V", true)){
                price = "200"
            }else if(args.currentReservation?.roomId!!.startsWith("S", true)){
                price = "100"
            }else if(args.currentReservation?.roomId!!.startsWith("P", true)){
                price = "300"
            }else{
                price = ""
            }
        }
        return price
    }
}