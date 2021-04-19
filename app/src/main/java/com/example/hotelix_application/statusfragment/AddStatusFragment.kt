package com.example.hotelix_application


import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hotelix_application.R
import com.example.hotelix_application.UpdateCheckInStatusFragmentArgs
import com.example.hotelix_application.database.status.Status
import com.example.hotelix_application.database.status.StatusViewModel
import com.example.hotelix_application.databinding.FragmentAddStatusBinding
import com.example.hotelix_application.paymentfragment.AddPaymentFragmentArgs
import kotlinx.android.synthetic.main.fragment_update_status.*


class AddStatusFragment : Fragment() {
    private val args by navArgs<AddStatusFragmentArgs>()
    private var _binding: FragmentAddStatusBinding? = null
    private val binding get() = _binding!!
    private lateinit var mStatusViewModel: StatusViewModel
//    private val args by navArgs<AddStatusFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddStatusBinding.inflate(inflater, container, false)
        binding.roomNameTextboxTextboxLayout.editText?.setText(args.currentStatus?.generatedRoomID)
        if(args.currentStatus?.status  == "checkout"){
            binding.updateRoomStatusTextboxLayout.editText?.setText("Room Not Cleaned")
        }
        mStatusViewModel = ViewModelProvider(this,).get(StatusViewModel::class.java)

//        binding.roomNameTextboxTextboxLayout.editText?.setText(args.)
//        binding.updateRoomStatusTextboxLayout.editText?.setText(args.)

        binding.addStatusButton.setOnClickListener{
            val roomName = binding.roomNameTextboxTextboxEdittext.text.toString()
            var roomStatus = binding.updateRoomStatusTextboxLayoutAutoCompleteTextView.text.toString()

//            if(inputCheck(roomName, roomStatus)){
//                roomStatus = checkStatus()
//                // create status object
//                val status = Status(0, Integer.parseInt(roomStatus), roomName, null)
//                mStatusViewModel.addStatus(status)
//                Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
//                findNavController().navigate(R.id.action_addStatusFragment_to_roomCleaningStatusFragment)
//            }else{
//                Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show()
//            }

            if(TextUtils.isEmpty(roomName)){
                Toast.makeText(requireContext(), "Please fill out Room Name field.", Toast.LENGTH_SHORT).show()
            }else if(TextUtils.isEmpty(roomStatus)){
                Toast.makeText(requireContext(), "Please fill out Room Status field.", Toast.LENGTH_SHORT).show()
            }else{
                roomStatus = checkStatus()
                // create status object
                val status = Status(0, Integer.parseInt(roomStatus), roomName, null)
                mStatusViewModel.addStatus(status)
                Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addStatusFragment_to_roomCleaningStatusFragment)
            }
        }

        return binding.root
    }

    override fun onResume() {
        val status = resources.getStringArray(R.array.status)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.status_dropdown, status)
        binding.updateRoomStatusTextboxLayoutAutoCompleteTextView?.setAdapter(arrayAdapter)
        super.onResume()
    }

//    private fun inputCheck(roomName: String, roomStatus: String):Boolean {
//        return !(TextUtils.isEmpty(roomName) && TextUtils.isEmpty(roomStatus))
//    }

    private fun checkStatus(): String{
        val status = update_room_status_textbox_layout_autoCompleteTextView?.text.toString()
        if(status == "Room Cleaned"){
            return "1"
        }else if(status == "In Process"){
            return "0"
        }else{
            return "-1"
        }
    }
}