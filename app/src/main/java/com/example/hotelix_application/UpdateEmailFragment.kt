package com.example.hotelix_application

import android.os.Bundle
import android.util.Patterns

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.hotelix_application.databinding.FragmentUpdateEmailBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_update_email.*

class UpdateEmailFragment : Fragment() {

    private var binding: FragmentUpdateEmailBinding? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var fStore : FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        val fragmentBinding = FragmentUpdateEmailBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()

        fStore = FirebaseFirestore.getInstance()

        binding = fragmentBinding

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutPassword.visibility = View.VISIBLE
        layoutUpdateEmail.visibility = View.GONE


        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            updateEmailFragment = this@UpdateEmailFragment
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun authenticateUserToUpdateEmail(){

        val password = edit_text_password.text.toString().trim()

        if(password.isNullOrEmpty()){
            edit_text_password.error = "Password required"
            edit_text_password.requestFocus()
        }
        else{

            auth.currentUser?.let{user->
                val credential = EmailAuthProvider.getCredential(user.email!!, password)

                progressbar.visibility = View.VISIBLE

                user.reauthenticate(credential)
                    .addOnCompleteListener { task ->

                        progressbar.visibility = View.GONE

                        when {
                            task.isSuccessful -> {
                                layoutPassword.visibility = View.GONE
                                layoutUpdateEmail.visibility = View.VISIBLE
                            }
                            task.exception is FirebaseAuthInvalidCredentialsException -> {
                                edit_text_password.error = "Invalid Password"
                                edit_text_password.requestFocus()
                            }
                            else -> {
                                Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            }
        }
    }

    fun updateEmail(){

        val email = edit_text_email.text.toString().trim()

        if(email.isNullOrEmpty()){
            edit_text_email.error = "Email Required"
            edit_text_email.requestFocus()
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edit_text_email.error = "Valid Email Required"
            edit_text_email.requestFocus()
        }

        progressbar.visibility = View.VISIBLE

        auth.currentUser?.let { user ->
            user.updateEmail(email)
                .addOnCompleteListener { task ->
                    progressbar.visibility = View.GONE

                    if(task.isSuccessful){

                        val authUser = auth.uid

                        val df = fStore.collection("Users").document(authUser!!)

                        val map : HashMap<String, Any> = HashMap()

                        map["userEmail"] = email

                        df.update(map)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Email Update Successful", Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.action_updateEmailFragment_to_managerProfileFragment)
                            }
                            .addOnFailureListener{
                                Toast.makeText(context, it.toString() , Toast.LENGTH_LONG).show()
                            }

                    }
                    else{
                        Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }

                }
        }

    }

}