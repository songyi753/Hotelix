package com.example.hotelix_application


import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import com.example.hotelix_application.databinding.FragmentLogInBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.hotelix_application.model.login.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_log_in.*

class LogInFragment : Fragment() {

    private val viewModel by viewModels<LoginViewModel>()
    private var binding: FragmentLogInBinding? = null
    private lateinit var navController: NavController
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //save inflated view to fragmentBinding
        val fragmentBinding = FragmentLogInBinding.inflate(inflater, container, false)

        fragmentBinding.loginProgress.visibility = View.INVISIBLE

        auth = FirebaseAuth.getInstance()

        fragmentBinding.forgetPassLinkClick.setOnClickListener{hyperLinkLinking()}

        fragmentBinding.loginBtn.setOnClickListener { launchSignInFlow() }
        //initialize to binding
        binding = fragmentBinding
        //create view with inflated fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeAuthenticationState()
        animation()

        navController = findNavController()

        setTextGradientColor()
    }

    private fun setTextGradientColor(){
        val text = application_name.paint
        val width = text.measureText("Hotelix")

        val shader = LinearGradient(0f, 0f, width, application_name.textSize, intArrayOf(
            Color.parseColor("#F97C3C"),
            Color.parseColor("#FDB54E"),
            /*Color.parseColor("#64B678"),
            Color.parseColor("#478AEA"),*/
            Color.parseColor("#8446CC")
        ),null, Shader.TileMode.REPEAT)

        application_name.paint.shader = shader
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun animation(){
        val ttb = AnimationUtils.loadAnimation(requireContext(), R.anim.top_to_btm)
        val stb = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_to_big)
        val btt = AnimationUtils.loadAnimation(requireContext(), R.anim.btm_to_top)

        val logo = loginIcon
        val appTitle = application_name

        appTitle.startAnimation(ttb)
        logo.startAnimation(ttb)

        val field1 = username
        val field2 = password

        field1.startAnimation(stb)
        field2.startAnimation(stb)

        val btn = login_btn
        val btn2 = forget_pass_link
        val btn3 = forget_pass_link_click

        btn.startAnimation(btt)
        btn2.startAnimation(btt)
        btn3.startAnimation(btt)
    }

    private fun launchSignInFlow(){
        val email = username.text.toString()
        val password = password.text.toString()

        login_progress.visibility = View.VISIBLE

        if(email.isNotEmpty() && password.isNotEmpty()){
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if(task.isSuccessful){

                            login_progress.visibility = View.INVISIBLE

                            Toast.makeText(context, "You are logged in!", Toast.LENGTH_SHORT).show()

                            val intent = Intent(context, MainActivity::class.java)
                            startActivity(intent)
                            activity?.finish()

                        }
                        else{

                            login_progress.visibility = View.INVISIBLE

                            Toast.makeText(
                                    context,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_LONG
                            ).show()
                        }
            }
        }

    }

    private fun observeAuthenticationState(){
        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when(authenticationState){
                LoginViewModel.AuthenticationState.AUTHENTICATED ->{
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                else -> {

                }
            }
        })
    }

    private fun hyperLinkLinking(){

        login_progress.visibility = View.INVISIBLE

        findNavController().navigate(R.id.action_logInFragment_to_forgetPasswordFragment)
    }

}