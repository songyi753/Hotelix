package com.example.hotelix_application

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hotelix_application.databinding.FragmentRoomAvailabilityBinding
import com.example.hotelix_application.model.ApplicationTaskViewModel


class RoomAvailabilityFragment : Fragment() {

    private var binding: FragmentRoomAvailabilityBinding? = null
    private val sharedViewModel : ApplicationTaskViewModel by activityViewModels()
    private val args by navArgs<RoomAvailabilityFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentRoomAvailabilityBinding.inflate(inflater, container, false)
        binding = fragmentBinding

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            //viewModel = sharedViewModel bind in xml
            roomAvailabilityFragment = this@RoomAvailabilityFragment //bind fragment in xml
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun navigate(choice : Int){

        when(choice){
            1 -> {
                //if else
                val rType = "Single Room"

                if(args.isReservation){
                    val bundle = bundleOf("rType" to rType, "isReservation" to true,"rType" to rType)
                    findNavController().navigate(R.id.action_roomAvailabilityFragment_to_roomFragment,bundle)

                }else{
                    val bundle = bundleOf("rType" to rType, "isReservation" to false)
                    findNavController().navigate(R.id.action_roomAvailabilityFragment_to_roomFragment,bundle)
                }
            }
            2 -> {
                //if else
                val rType = "Vip Room"
                var bundle = bundleOf("rType" to rType)
                if(args.isReservation){
                    bundle = bundleOf("isReservation" to true,"rType" to rType)
                    findNavController().navigate(R.id.action_roomAvailabilityFragment_to_roomFragment,bundle)

                }else{
                    findNavController().navigate(R.id.action_roomAvailabilityFragment_to_roomFragment,bundle)
                }

            }
            3 -> {
                //if else

                val rType = "President Room"
                var bundle = bundleOf("rType" to rType)
                if(args.isReservation){
                    var bundle = bundleOf("isReservation" to true,"rType" to rType)

                    findNavController().navigate(R.id.action_roomAvailabilityFragment_to_roomFragment,bundle)

                }else{
                    findNavController().navigate(R.id.action_roomAvailabilityFragment_to_roomFragment,bundle)
                }

            }
        }
    }

}