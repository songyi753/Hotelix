package com.example.hotelix_application

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hotelix_application.database.HotelixDatabase
import com.example.hotelix_application.database.reservation.ReservationList
import com.example.hotelix_application.databinding.FragmentEditReservationBinding
import com.example.hotelix_application.model.reservation.ReservationListViewModel
import com.example.hotelix_application.model.reservation.ReservationListViewModelFactory
import kotlinx.android.synthetic.main.fragment_add_reservation.*
import kotlinx.android.synthetic.main.fragment_add_reservation.contact_number
import kotlinx.android.synthetic.main.fragment_add_reservation.customer_name
import kotlinx.android.synthetic.main.fragment_add_reservation.reservation_date
import kotlinx.android.synthetic.main.fragment_add_reservation.reservation_id
import kotlinx.android.synthetic.main.fragment_edit_reservation.*
import java.util.*

class EditReservationFragment : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private val args by navArgs<EditReservationFragmentArgs>()
    private var binding: FragmentEditReservationBinding? = null
    private lateinit var sharedViewModel: ReservationListViewModel

    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0

    var stringDay = ""
    var stringMonth = ""
    var stringYear = ""
    var stringHour = ""
    var stringMinute = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentEditReservationBinding.inflate(inflater, container, false)

        fragmentBinding.reservationId.setText(args.currentReservation.generatedID)
        fragmentBinding.customerName.setText(args.currentReservation.name)
        fragmentBinding.reservationDate.setText("Reserve Date: " + args.currentReservation.reserveDate.toString())
        fragmentBinding.contactNumber.setText(args.currentReservation.contactNumber.toString())

        binding = fragmentBinding

        val application = requireNotNull(this.activity).application

        val dataSource = HotelixDatabase.getInstance(application).reservationListDao
        val viewModelFactory = ReservationListViewModelFactory(dataSource, application)

        sharedViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(ReservationListViewModel::class.java)

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            reservationListViewModel = sharedViewModel
            editReservationFragment = this@EditReservationFragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    //date
    private fun getDateTimeCalendar(){
        val cal : Calendar = Calendar.getInstance()

        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun pickDate(){
        reservation_date.setOnClickListener{
            getDateTimeCalendar()
            DatePickerDialog(requireActivity(), this, year, month, day).show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        getDateTimeCalendar()
        TimePickerDialog(requireActivity(), this, hour, minute, true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour = hourOfDay
        savedMinute = minute

        stringDay = if(savedDay.toString().length == 1)
        {
            "0$savedDay"
        }
        else{
            "$savedDay"
        }

        stringMonth = if(savedMonth.toString().length == 1){
            "0$savedMonth"
        }
        else{
            "$savedMonth"
        }

        stringYear = if(savedYear.toString().length == 1){
            "0$savedYear"
        }
        else{
            "$savedYear"
        }

        stringHour = if(savedHour.toString().length == 1){
            "0$savedHour"
        }
        else{
            "$savedHour"
        }

        stringMinute = if(savedMinute.toString().length == 1){
            "0$savedMinute"
        }
        else{
            "$savedMinute"
        }

        reservation_date.setText("$stringDay-$stringMonth-$stringYear $stringHour:$stringMinute")
    }

    private fun validationContactNum(conNum : String): Boolean {
        return if(conNum.length < 6 || conNum.length > 13){
            false
        }
        else{
            android.util.Patterns.PHONE.matcher(conNum).matches();
        }
    }

    fun updateReservation(){

        val reserveId = args.currentReservation.reserveID
        val generatedId = args.currentReservation.generatedID.toString()
        val name = customer_name.text.toString()
        val date = args.currentReservation.reserveDate
        val contact = contact_number.text.toString()
        val roomType = args.currentReservation.roomId

        when {
            name.trim().isNullOrEmpty() -> {
                Toast.makeText(activity, "Customer name is required", Toast.LENGTH_SHORT).show()
            }
//            date.trim().isNullOrEmpty() -> {
//                Toast.makeText(activity, "Reservation date is required", Toast.LENGTH_SHORT).show()
//            }
            contact.trim().isNullOrEmpty() -> {
                Toast.makeText(activity, "Customer contact is required", Toast.LENGTH_SHORT).show()
            }
            else -> {
                if(validationContactNum(contact)) {
                    var reservationDetail = ReservationList(reserveId, generatedId, name, date, roomType,contact)

                    sharedViewModel.updateReservationToDatabase(reservationDetail)

                    Toast.makeText(activity, "Reservation successfully updated", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context, "Invalid Contact Number, Please Try Again", Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    fun cancelModify(){
        findNavController().navigate(R.id.action_editReservationFragment_to_reservationListFragment)
    }

   fun deleteReservation(){ //for checked in reservation
       val builder = AlertDialog.Builder(requireContext())
       builder.setPositiveButton("Yes"){_, _ ->
           sharedViewModel.deleteReservationFromDatabase(args.currentReservation)
           Toast.makeText(context, "Successfully removed: ${args.currentReservation.reserveID}", Toast.LENGTH_SHORT).show()
           findNavController().navigate(R.id.action_editReservationFragment_to_reservationListFragment)
       }
       builder.setNegativeButton("No"){_, _ -> }
       builder.setTitle("Delete Reservation Record")
       builder.setMessage("Are you sure you want to delete ${args.currentReservation.generatedID} reservation record?")
       builder.create().show()
   }

    fun setRoomId(){

        val builder = AlertDialog.Builder(context)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_text, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.edit_text)

        val reserveId = args.currentReservation.reserveID
        val generatedId = args.currentReservation.generatedID
        val name = args.currentReservation.name
        val date = args.currentReservation.reserveDate
        val contact = args.currentReservation.contactNumber

        with(builder){
            setTitle("Enter Reservation Room ID, this is for modification of reservation's room id in case needed")
            setPositiveButton("Confirm"){_, _ ->
                if(editText.text.toString().trim().isEmpty()){

                    Toast.makeText(activity, "Field cannot be empty", Toast.LENGTH_SHORT).show()
                }
                else{

                    val updateRoomId = ReservationList(reserveId, generatedId, name, date, editText.text.toString(),contact)

                    sharedViewModel.updateReservationToDatabase(updateRoomId)

                    Toast.makeText(activity, "Reservation room id successfully updated", Toast.LENGTH_SHORT).show()
                }
            }
            setNegativeButton("Cancel"){_, _->

            }
            setView(dialogLayout)
            show()
        }
    }
}