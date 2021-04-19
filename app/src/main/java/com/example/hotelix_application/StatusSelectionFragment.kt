package com.example.hotelix_application

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.hotelix_application.databinding.FragmentRoomAvailabilityBinding
import com.example.hotelix_application.databinding.FragmentStatusSelectionBinding
import com.example.hotelix_application.model.ApplicationTaskViewModel


class StatusSelectionFragment : Fragment() {

    private var binding: FragmentStatusSelectionBinding? = null
    private val sharedViewModel : ApplicationTaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentStatusSelectionBinding.inflate(inflater, container, false)
        binding = fragmentBinding

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            //viewModel = sharedViewModel bind in xml
            statusSelectionFragment = this@StatusSelectionFragment //bind fragment in xml
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
                val status = "Pending"
                val bundle = bundleOf("status" to status)
                findNavController().navigate(R.id.action_statusSelectionFragment_to_checkInFragment,bundle)

            }
            2 -> {
                //if else
                val status = "Checked In"
                val bundle = bundleOf("status" to status)
                findNavController().navigate(R.id.action_statusSelectionFragment_to_checkInFragment,bundle)

            }
        }
    }
}