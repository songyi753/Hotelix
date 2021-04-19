package com.example.hotelix_application.statusfragment

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hotelix_application.R
import com.example.hotelix_application.database.status.Status
import com.example.hotelix_application.database.status.StatusViewModel
import com.example.hotelix_application.databinding.FragmentUpdateStatusBinding
import com.example.hotelix_application.statusfragment.UpdateStatusFragmentArgs
import kotlinx.android.synthetic.main.fragment_update_status.*

class UpdateStatusFragment : Fragment() {

    private val args by navArgs<UpdateStatusFragmentArgs>()
    private var _binding: FragmentUpdateStatusBinding? = null
    private val binding get() = _binding!!
    private lateinit var mStatusViewModel: StatusViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateStatusBinding.inflate(inflater, container, false)

        mStatusViewModel = ViewModelProvider(this).get(StatusViewModel::class.java)

        val statusString = getStatusString()

        binding.updateRoomNameTextboxLayout.editText?.setText(args.currentStatus.roomName)
        binding.updateRoomStatusTextboxLayout.editText?.setText(statusString)
        binding.updateCleanerNameTextboxLayout.editText?.setText(args.currentStatus.cleanerName)

        binding.updateStatusButton.setOnClickListener {
            updateStatus()
        }

        //set when the status == 1, only the cleaner name allow to input
        binding.updateCleanerNameTextboxLayout.isEnabled = args.currentStatus.roomCleaningStatus == -1

        //Add menu
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onResume() {
        val status = resources.getStringArray(R.array.status)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.status_dropdown, status)
        binding.updateRoomStatusTextboxLayoutAutoCompleteTextView?.setAdapter(arrayAdapter)
        super.onResume()
    }

    private fun updateStatus() {
        val roomName = update_room_name_textbox_edittext.text.toString()
        var roomStatus = update_room_status_textbox_layout_autoCompleteTextView?.text.toString()
        val cleanerName = update_cleaner_name_textbox_edittext.text.toString()
//
//        if (inputCheck(roomName, roomStatus)) {
//            roomStatus = checkStatus()
//            //create status object
//            val updatedStatus = Status(args.currentStatus.roomId, Integer.parseInt(roomStatus), roomName, cleanerName)
//            //update current status
//            mStatusViewModel.updateStatus(updatedStatus)
//            Toast.makeText(requireContext(), "Updated successfully!", Toast.LENGTH_SHORT).show()
//            //navigate back
//            findNavController().navigate(R.id.action_updateStatusFragment_to_roomCleaningStatusFragment)
//        } else {
//            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT)
//                .show()
//        }

        if(TextUtils.isEmpty(roomName)){
            Toast.makeText(requireContext(), "Please fill out Room Name field.", Toast.LENGTH_SHORT).show()
        }else if(TextUtils.isEmpty(roomStatus)){
            Toast.makeText(requireContext(), "Please fill out Room Status field.", Toast.LENGTH_SHORT).show()
        }else{
            roomStatus = checkStatus()
            //create status object
            val updatedStatus = Status(args.currentStatus.roomId, Integer.parseInt(roomStatus), roomName, cleanerName)
            //update current status
            mStatusViewModel.updateStatus(updatedStatus)
            Toast.makeText(requireContext(), "Updated successfully!", Toast.LENGTH_SHORT).show()
            //navigate back
            findNavController().navigate(R.id.action_updateStatusFragment_to_roomCleaningStatusFragment)
        }
    }

//    private fun inputCheck(roomName: String, roomStatus: String): Boolean {
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

    private fun getStatusString(): String{
        val status = args.currentStatus.roomCleaningStatus.toString()
        if(status == "1"){
            return "Room Cleaned"
        }else if(status == "0"){
            return "In Process"
        }else{
            return "Room Not Cleaned"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_status_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.status_menu_delete) {
            deleteUser()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteUser() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mStatusViewModel.deleteStatus(args.currentStatus)
            Toast.makeText(
                requireContext(),
                "Successfully removed ${args.currentStatus.roomName}",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateStatusFragment_to_roomCleaningStatusFragment)
        }
        builder.setNegativeButton("No") { _, _ ->

        }
        builder.setTitle("Delete ${args.currentStatus.roomName}?")
        builder.setMessage("Are you sure you want to delete ${args.currentStatus.roomName}?")
        builder.create().show()
    }
}