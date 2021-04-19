package com.example.hotelix_application

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.hotelix_application.database.HotelixDatabase
import com.example.hotelix_application.database.reservation.ReservationList
import com.example.hotelix_application.databinding.FragmentAddReservationBinding
import com.example.hotelix_application.model.reservation.ReservationListViewModel
import com.example.hotelix_application.model.reservation.ReservationListViewModelFactory
import kotlinx.android.synthetic.main.fragment_add_reservation.*
import java.util.*

class AddReservationFragment : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private var binding: FragmentAddReservationBinding? = null
    private lateinit var sharedViewModel: ReservationListViewModel
    private var latestReservation = ReservationList(0L, "", "", "", "", "")

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
        val fragmentBinding = FragmentAddReservationBinding.inflate(inflater, container, false)
        binding = fragmentBinding

        val application = requireNotNull(this.activity).application

        //create instance of ViewModel Factory
        val dataSource = HotelixDatabase.getInstance(application).reservationListDao
        val viewModelFactory = ReservationListViewModelFactory(dataSource, application)

        sharedViewModel =
                ViewModelProvider(
                        this, viewModelFactory).get(ReservationListViewModel::class.java)

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        pickDate()

        binding?.apply{
            lifecycleOwner = viewLifecycleOwner
            reservationListViewModel = sharedViewModel
            addReservationFragment = this@AddReservationFragment
        }

        sharedViewModel.reservationList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it.let {
                if(it.isNotEmpty()){
                    latestReservation = it.last()
                }
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    //Date
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


    fun addReservation(){

        val name = customer_name.text.toString()
        val contact = contact_number.text.toString()
        val date = "$stringDay-$stringMonth-$stringYear $stringHour:$stringMinute"
        var nextReserveId: Int

        nextReserveId = if(latestReservation.reserveID != 0L){
            latestReservation.reserveID.toInt() + 1
        } else{
            1
        }


        val customId = createCustomId(nextReserveId)

        when {
            name.trim().isNullOrEmpty() -> {
                Toast.makeText(activity, "Customer name is required", Toast.LENGTH_SHORT).show()
            }
            date.trim().isNullOrEmpty() -> {
                Toast.makeText(activity, "Reservation date is required", Toast.LENGTH_SHORT).show()
            }
            contact.trim().isNullOrEmpty() -> {
                Toast.makeText(activity, "Customer contact is required", Toast.LENGTH_SHORT).show()
            }
            else -> {
                if(validationContactNum(contact)){

                    var reservationDetail = ReservationList(0, customId, name, date, "", contact)

                    sharedViewModel.addReservationToDatabase(reservationDetail)

                    Toast.makeText(activity, "Select Room Type", Toast.LENGTH_SHORT).show()

                    selectRoomToReservation()

                }
                else{
                    Toast.makeText(context, "Invalid Contact Number, Please Try Again", Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    private fun createCustomId(nextReserveId : Int) : String{
        var generatedId = "C00$nextReserveId"

        if(generatedId.length > 4 && generatedId.length < 5){
            generatedId = "C0$nextReserveId"
        }
        else if (generatedId.length > 5){
            generatedId = "C$nextReserveId"
        }

        return generatedId

    }

    private fun selectRoomToReservation(){
        val action = AddReservationFragmentDirections.actionAddReservationFragmentToRoomAvailabilityFragment(true)
        findNavController().navigate(action)
    }
}

