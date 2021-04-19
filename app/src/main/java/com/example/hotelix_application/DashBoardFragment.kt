package com.example.hotelix_application

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.hotelix_application.database.users.UserList
import com.example.hotelix_application.databinding.FragmentDashBoardBinding
import com.example.hotelix_application.model.ApplicationTaskViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_dash_board.*

class DashBoardFragment : Fragment() {

    private var binding: FragmentDashBoardBinding? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var fStore : FirebaseFirestore
    //private lateinit var storeData : UserList

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentDashBoardBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()

        fStore = FirebaseFirestore.getInstance()

        binding = fragmentBinding

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkAccessLevel()


        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            dashboardFragment = this@DashBoardFragment //bind fragment in xml
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    fun navigate(choice : Int){

        when(choice){
            1 -> {
//                val action = DashBoardFragmentDirections.actionDashBoardFragmentToManagerProfileFragment(storeData) //args rebuild project
//                findNavController().navigate(action)
                findNavController().navigate(R.id.action_dashBoardFragment_to_managerProfileFragment)
            }
            2 -> {
                //room record
                findNavController().navigate(R.id.action_dashBoardFragment_to_roomAvailabilityFragment)
            }
            3 -> {
                findNavController().navigate(R.id.action_dashBoardFragment_to_reservationListFragment)
            }
            4 -> {
                //clock in and out
                findNavController().navigate(R.id.action_dashBoardFragment_to_clockInAndOutFragment)
            }
            5 -> {
                findNavController().navigate(R.id.action_dashBoardFragment_to_searchPaymentFragment)
            }
            6 -> {
                findNavController().navigate(R.id.action_dashBoardFragment_to_roomCleaningStatusFragment)
            }
            7 -> {
                findNavController().navigate(R.id.action_dashBoardFragment_to_statusSelectionFragment)
            }
            8 -> {

                findNavController().navigate(R.id.action_dashBoardFragment_to_manageDeleteStaffFragment)
            }
        }
    }

    private fun checkAccessLevel(){

        val user = auth.uid


        //SAME MEANING AS
        //val df = fStore.collection("").document(user!!) <<< UID

        val df = user?.let { fStore.collection("Users").document(it) }

        df?.get()?.addOnCompleteListener {
            if(it.isSuccessful){
                staff.isEnabled = it.result!!.data!!.getValue("isAdmin") == "1"

//                val name = it.result!!.data!!.getValue("name").toString()
//                val position = it.result!!.data!!.getValue("position").toString()
//                val userEmail = it.result!!.data!!.getValue("userEmail").toString()
//                val userId = it.result!!.data!!.getValue("userId").toString()

                //storeData = UserList(name, position, userEmail, userId
            }
        }

    }

}