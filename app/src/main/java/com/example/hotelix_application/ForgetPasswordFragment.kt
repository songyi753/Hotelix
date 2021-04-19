package com.example.hotelix_application

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.hotelix_application.databinding.FragmentForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_forget_password.*
//import kotlinx.android.synthetic.main.fragment_task_list.*


class ForgetPasswordFragment : Fragment() {

    private var binding: FragmentForgetPasswordBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            forgetPasswordFragment = this@ForgetPasswordFragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun handleSendEmail(){
        val email = email.text.toString().trim{ it <= ' '}

        if(email.isEmpty()){
            Toast.makeText(
                    context,
                    "Please enter email address",
                    Toast.LENGTH_SHORT
            ).show()
        }else{
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            Toast.makeText(
                                    context,
                                    "Please check your email to reset your password!",
                                    Toast.LENGTH_LONG
                            ).show()

                            findNavController().navigate(R.id.action_forgetPasswordFragment_to_logInFragment)
                        }
                        else{
                            Toast.makeText(
                                    context,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_LONG
                            ).show()
                        }
                    }
        }
    }
}