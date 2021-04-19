package com.example.hotelix_application

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hotelix_application.databinding.FragmentAddStaffBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.fragment_add_staff.*


class AddStaffFragment : Fragment() {
    private var binding: FragmentAddStaffBinding? = null
    private lateinit var mAuth : FirebaseAuth
    private lateinit var fStore : FirebaseFirestore
    private var index : Int? = 0
    private var validAdmin : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentAddStaffBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AddStaffButton.setOnClickListener{
            //valid
            mAuth = FirebaseAuth.getInstance()
            fStore = FirebaseFirestore.getInstance()

            fStore.collection("Users").get().addOnCompleteListener {
                if (it.isSuccessful) {
                    var count = it.getResult()?.size()
                    if (count != null) {
                        getIndex(count)
                    }
                    Log.d("TAG", index.toString() + "")
                } else {
                    getIndex(0)
                }
            }

            val name = EditTextStaffName.text.toString().trim()
            val email = EditTextLoginID.text.toString().trim()
            val password = EditTextPassword.text.toString().trim()
            val adminPassword = EditTextComfirmPassword.text.toString().trim()

            if(name.isEmpty() || email.isEmpty() || password.isEmpty() || adminPassword.isEmpty()) {
                Toast.makeText(
                    getActivity(),
                    "Fill up all the field",
                    Toast.LENGTH_LONG
                ).show()
            }else{
                registerUser(name, email, password, adminPassword)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun registerUser(
        name: String,
        userEmail: String,
        password: String,
        adminPassword: String
    ){
        valid(adminPassword)
        if(validAdmin) {

            mAuth = FirebaseAuth.getInstance()

            val currentUser = mAuth.currentUser
            val email = currentUser.email

            mAuth.signOut()

            mAuth.createUserWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener() {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            getActivity(),
                            "Successful Add to Authentication",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            getActivity(),
                            "Failed to add into Authentication",
                            Toast.LENGTH_LONG
                        ).show()
                        findNavController().navigate(R.id.action_addStaffFragment_to_manageDeleteStaffFragment)
                    }
                }
            mAuth.signInWithEmailAndPassword(userEmail, password).addOnCompleteListener() {
                addUserToFireStore(name, userEmail)
            }

            mAuth.signOut()

            mAuth.signInWithEmailAndPassword(email, adminPassword).addOnCompleteListener {

                mAuth.currentUser.let { user ->
                    val credential = EmailAuthProvider.getCredential(email, adminPassword)
                    user.reauthenticate(credential).addOnCompleteListener { task ->
                        when {
                            task.isSuccessful -> {
                                Toast.makeText(
                                    getActivity(),
                                    "Process Successful",
                                    Toast.LENGTH_LONG
                                ).show()
                                findNavController().navigate(R.id.action_addStaffFragment_to_manageDeleteStaffFragment)
                            }
                            else -> {
                                Toast.makeText(
                                    getActivity(),
                                    "Process Failed",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            }


        }

    }

    fun addUserToFireStore(name: String, userEmail: String){

        fStore = FirebaseFirestore.getInstance()
        var user = hashMapOf(
            "isAdmin" to "0",
            "name" to name,
            "position" to "Staff",
            "userEmail" to userEmail,
            "userId" to "S$index"
        )


        fStore.collection("Users").document(mAuth.getUid().toString())
        .set(user as Map<String, Any>)
        .addOnSuccessListener {
            Toast.makeText(getActivity(), "Successful add to Firestore", Toast.LENGTH_LONG).show()
        }
    }

    fun authenticateUser(valid: Boolean){
        if(valid){
            validAdmin = true
        }else{
            validAdmin = false
        }
    }

    fun getIndex(ind: Int){
        index = ind
    }

    fun valid(adminPassword: String){

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val email = currentUser.email

        mAuth.currentUser.let { user ->
            val credential = EmailAuthProvider.getCredential(email, adminPassword)
            user.reauthenticate(credential).addOnCompleteListener { task ->
                when {
                    task.isSuccessful -> {
                        Toast.makeText(
                            getActivity(),
                            "Verify Successful",
                            Toast.LENGTH_LONG
                        ).show()
                        authenticateUser(true)
                    }
                    else -> {
                        authenticateUser(false)
                    }
                }
            }
        }
    }


}