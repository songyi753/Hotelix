package com.example.hotelix_application

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hotelix_application.database.HotelixDatabase
import com.example.hotelix_application.database.Rooms.RoomsList
import com.example.hotelix_application.database.reservation.ReservationList
import com.example.hotelix_application.databinding.FragmentEditRoomBinding
import kotlinx.android.synthetic.main.fragment_update_check_in_status.*
import com.example.hotelix_application.model.reservation.ReservationListViewModel
import com.example.hotelix_application.model.reservation.ReservationListViewModelFactory
import com.example.hotelix_application.model.rooms.RoomsListViewModel
import com.example.hotelix_application.model.rooms.RoomsListViewModelFactory
import kotlinx.android.synthetic.main.fragment_edit_reservation.*


class EditRoomFragment : Fragment() {

    private var binding: FragmentEditRoomBinding? = null
    private val args by navArgs<EditRoomFragmentArgs>()
    private lateinit var sharedViewModel: RoomsListViewModel
    private lateinit var reservationViewModel: ReservationListViewModel
    private var currentReservation : ReservationList? = null

    override fun onResume() {
        super.onResume()
        val roomStatus = resources.getStringArray(R.array.roomStatus)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, roomStatus)
        binding?.autoCompleteTextView?.setAdapter(arrayAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentEditRoomBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        fragmentBinding.ReserveIDField.setText(args.currentRoom.generatedID)

        val application = requireNotNull(this.activity).application

        val dataSource = HotelixDatabase.getInstance(application).roomsListDao
        val viewModelFactory = RoomsListViewModelFactory(dataSource, application)

        sharedViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(RoomsListViewModel::class.java)

        val dataSource2 = HotelixDatabase.getInstance(application).reservationListDao
        val viewModelFactory2 = ReservationListViewModelFactory(dataSource2, application)

        reservationViewModel =
            ViewModelProvider(
                this, viewModelFactory2).get(ReservationListViewModel::class.java)

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val roomStatus = resources.getStringArray(R.array.reservePending)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, roomStatus)
        autoCompleteTextView?.setAdapter(arrayAdapter)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            editRoomFragment = this@EditRoomFragment
        }

        if(args.currentRoom.isReservation){
            setReservationRoomType()

            reservationViewModel.reservationList.observe(viewLifecycleOwner, Observer {
                it?.let {
                    currentReservation = it.last()
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

//    fun updateStatus() {
//        val reserveID = args.currentRoom.reserveID
//        var generatedID = args.currentRoom.generatedID
//        val status = autoCompleteTextView.text.toString()
//        val roomType = args.currentRoom.roomType
//
//        val roomList = RoomsList(reserveID,generatedID,roomType,status)
//        sharedViewModel.updateRoomsToDatabase(roomList)
//
//        Toast.makeText(context,"Successfully updated", Toast.LENGTH_LONG).show()
////        findNavController().navigate(R.id.action_updateCheckInStatusFragment_to_checkInFragment)
//    }
    fun nav(){
        findNavController().navigate(R.id.action_editRoomFragment_to_dashBoardFragment)
    }

    private fun setReservationRoomType(){
        val roomGeneratedId = args.currentRoom.generatedID

        reservationViewModel.updateRoomType(roomGeneratedId)
    }

    fun updateRoomStatus(){
        if(args.currentRoom.isReservation && currentReservation != null){

            val currentReservationGeneratedId = currentReservation!!.generatedID //pass this into update room to set reserve id
//
//          sharedViewModel.updateRoomsToDatabase()
//
            val action = EditRoomFragmentDirections.actionEditRoomFragmentToAddPaymentFragment(
                currentReservation!!
            )

            val reserveID = args.currentRoom.reserveID
            var generatedID = args.currentRoom.generatedID
            val status = autoCompleteTextView.text.toString()
            val roomType = args.currentRoom.roomType

            val roomList = RoomsList(reserveID,generatedID,roomType,status)
            sharedViewModel.updateRoomsToDatabase(roomList)
            findNavController().navigate(action)
        }
        else{
            val reserveID = args.currentRoom.reserveID
            var generatedID = args.currentRoom.generatedID
            val status = autoCompleteTextView.text.toString()
            val roomType = args.currentRoom.roomType

            val roomList = RoomsList(reserveID,generatedID,roomType,status)
            sharedViewModel.updateRoomsToDatabase(roomList)

            Toast.makeText(context,"Successfully updated", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_editRoomFragment_to_roomAvailabilityFragment)
        }
    }
}