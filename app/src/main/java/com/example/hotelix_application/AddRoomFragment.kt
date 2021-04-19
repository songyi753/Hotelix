package com.example.hotelix_application


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hotelix_application.adapters.RoomsListAdapter
import com.example.hotelix_application.database.HotelixDatabase
import com.example.hotelix_application.database.Rooms.RoomsList
import com.example.hotelix_application.databinding.FragmentAddRoomBinding
import com.example.hotelix_application.model.rooms.RoomsListViewModel
import com.example.hotelix_application.model.rooms.RoomsListViewModelFactory
import kotlinx.android.synthetic.main.fragment_add_room.*
import kotlinx.android.synthetic.main.fragment_add_room.view.*
import kotlinx.android.synthetic.main.rooms_list_item_view.*


class AddRoomFragment : Fragment() {
    private var binding: FragmentAddRoomBinding? = null
    private lateinit var sharedViewModel: RoomsListViewModel
    private val args by navArgs<AddRoomFragmentArgs>()
    private val adapter: RoomsListAdapter by lazy { RoomsListAdapter() }
    override fun onResume() {
        super.onResume()
        if(args.roomExtra.roomType=="single"){

            val roomTypes = resources.getStringArray(R.array.single)
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, roomTypes)
            binding?.autoCompleteTextView?.setAdapter(arrayAdapter)
        }else if(args.roomExtra.roomType=="vip"){
            val roomTypes = resources.getStringArray(R.array.vip)
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, roomTypes)
            binding?.autoCompleteTextView?.setAdapter(arrayAdapter)
        }else{
            val roomTypes = resources.getStringArray(R.array.president)
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, roomTypes)
            binding?.autoCompleteTextView?.setAdapter(arrayAdapter)
        }


    }
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentAddRoomBinding.inflate(inflater, container, false)
        if(args.roomExtra.roomType=="single"){

            fragmentBinding.autoCompleteTextView.setText("single")
        }else if(args.roomExtra.roomType=="vip"){
            fragmentBinding.autoCompleteTextView.setText("vip")
        }else{
            fragmentBinding.autoCompleteTextView.setText("president")
        }
        binding = fragmentBinding


        val application = requireNotNull(this.activity).application

        //create instance of ViewModel Factory
        val dataSource = HotelixDatabase.getInstance(application).roomsListDao
        val viewModelFactory = RoomsListViewModelFactory(dataSource, application)

        sharedViewModel =
                ViewModelProvider(
                        this, viewModelFactory).get(RoomsListViewModel::class.java)

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding?.apply{
            lifecycleOwner = viewLifecycleOwner
            roomsListViewModel = sharedViewModel
            addRoomFragment = this@AddRoomFragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun addRoom(){
//        val textInputLayout: TextInputLayout = findViewById(R.id.roomType)
//        val text: String = textInputLayout.editText!!.text

        val type = autoCompleteTextView.text.toString()
        when {
            type.trim().isNullOrEmpty() -> {
                Toast.makeText(activity, "Room type is required", Toast.LENGTH_SHORT).show()
            }
            else -> {

                var count = args.roomExtra.count
                var nextCount = count?.plus(1)
                var roomT =""
                if(type == "single"){
                    roomT ="S"
                }else if(type == "vip"){
                    roomT ="V"
                }else
                    roomT ="P"

                var roomDetail = RoomsList(0, roomT +count.toString(), type, "checkout")

                sharedViewModel.addRoomsToDatabase(roomDetail)
//                createCustomId()
                Toast.makeText(activity, "Room successfully added", Toast.LENGTH_SHORT).show()
                navigate()
            }
        }
    }

    fun navigate(){
        findNavController().navigate(R.id.action_addRoomFragment_to_roomAvailabilityFragment)
    }

//    private fun createCustomId(){
//        sharedViewModel.createCustomIdAndStore()
//    }
}