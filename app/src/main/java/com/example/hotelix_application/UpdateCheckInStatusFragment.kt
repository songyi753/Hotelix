package com.example.hotelix_application

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hotelix_application.database.HotelixDatabase
import com.example.hotelix_application.database.Rooms.RoomsList
import com.example.hotelix_application.database.reservation.ReservationList
import com.example.hotelix_application.database.status.Status
import com.example.hotelix_application.database.status.StatusViewModel
import com.example.hotelix_application.databinding.FragmentUpdateCheckInStatusBinding
import com.example.hotelix_application.model.reservation.ReservationListViewModel
import com.example.hotelix_application.model.reservation.ReservationListViewModelFactory
import com.example.hotelix_application.model.rooms.RoomsListViewModel
import com.example.hotelix_application.model.rooms.RoomsListViewModelFactory
import com.example.hotelix_application.paymentfragment.DisplayPaymentInfoFragmentArgs
import com.example.hotelix_application.statusfragment.UpdateStatusFragmentArgs
import kotlinx.android.synthetic.main.fragment_dash_board.*
import kotlinx.android.synthetic.main.fragment_update_check_in_status.*
import kotlinx.android.synthetic.main.fragment_update_status.*


class UpdateCheckInStatusFragment : Fragment() {
    private var binding: FragmentUpdateCheckInStatusBinding? = null
    //private val reservationViewModel :  by activityViewModels()
    private val args by navArgs<UpdateCheckInStatusFragmentArgs>()
    private lateinit var sharedViewModel: RoomsListViewModel
    private lateinit var reservationViewModel: ReservationListViewModel
    private var list= ReservationList(0L,"","","","","")
//    override fun onResume() {
//        super.onResume()
//        val roomStatus = resources.getStringArray(R.array.roomStatus)
//        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, roomStatus)
//        binding?.autoCompleteTextView?.setAdapter(arrayAdapter)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentUpdateCheckInStatusBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        fragmentBinding.roomId.text = args.currentCheckIn.generatedRoomID

        val application = requireNotNull(this.activity).application


        //create instance of ViewModel Factory
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

        val roomStatus = resources.getStringArray(R.array.roomStatus)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, roomStatus)
        autoCompleteTextView?.setAdapter(arrayAdapter)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            //reserveViewModel = reservationViewModel
            updateCheckInStatusFragment = this@UpdateCheckInStatusFragment
        }

        reservationViewModel.reservationList.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it.isNotEmpty()){
                    for (i in it){
                        if(i.roomId==args.currentCheckIn.generatedRoomID){

                            binding?.ReserveIDField?.setText(i.reserveID.toString())
                            binding?.customerNameField?.setText(i.name)
                            binding?.reserveDateField?.setText(i.reserveDate.toString())
                            binding?.contactNumberField?.setText(i.contactNumber)
                        }
                    }

                }

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun updateStatus() {
        val reserveID = args.currentCheckIn.roomID
        var generatedID = args.currentCheckIn.generatedRoomID
        val status = autoCompleteTextView.text.toString()
        val roomType = args.currentCheckIn.roomType

        val roomList = RoomsList(reserveID,generatedID,roomType,status)
        sharedViewModel.updateRoomsToDatabase(roomList)

        Toast.makeText(context,"Successfully updated",Toast.LENGTH_LONG).show()
        val action = UpdateCheckInStatusFragmentDirections.actionUpdateCheckInStatusFragmentToAddStatusFragment(roomList)
        if(autoCompleteTextView.text.toString()=="checkout"){
            findNavController().navigate(action)
        }
        else
            findNavController().navigate(UpdateCheckInStatusFragmentDirections.actionUpdateCheckInStatusFragmentToStatusSelectionFragment())


    }
    fun nav(){
        findNavController().navigate(UpdateCheckInStatusFragmentDirections.actionUpdateCheckInStatusFragmentToDashBoardFragment())}
}