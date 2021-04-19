package com.example.hotelix_application

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.hotelix_application.databinding.FragmentManagerProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_dash_board.*
import kotlinx.android.synthetic.main.fragment_manager_profile.*


class ManagerProfileFragment : Fragment() {

    private var binding: FragmentManagerProfileBinding? = null
    //private val args by navArgs<ManagerProfileFragmentArgs>()
    private lateinit var auth : FirebaseAuth
    private lateinit var fStore : FirebaseFirestore
    private lateinit var storageReference : FirebaseStorage
    private var imageUri : Uri ?= null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentManagerProfileBinding.inflate(inflater, container, false)

        fragmentBinding.progressBar.visibility = View.INVISIBLE
        fragmentBinding.uploadImageBtn.visibility = View.INVISIBLE

        auth = FirebaseAuth.getInstance()

        fStore = FirebaseFirestore.getInstance()

        storageReference = FirebaseStorage.getInstance()

        binding = fragmentBinding

        return fragmentBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadProfileImage()
        getUserDataFromFireStore()
        profileAnim()

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            managerProfileFragment = this@ManagerProfileFragment
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun profileAnim(){
        val stb = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_to_big)

        val profileImg = profile_picture

        profileImg.startAnimation(stb)
    }

    private fun getUserDataFromFireStore(){

        val user = auth.uid

        val df = user?.let { fStore.collection("Users").document(it) }

        df?.get()?.addOnCompleteListener {
            if(it.isSuccessful){
                val name = it.result!!.data!!.getValue("name").toString()
                val position = it.result!!.data!!.getValue("position").toString()
                val userEmail = it.result!!.data!!.getValue("userEmail").toString()
                val userId = it.result!!.data!!.getValue("userId").toString()

                manager_id.text = userId
                manager_position.text = "Position : $position"
                manager_name.text = "Name : $name"
                manager_email.text = "Email : $userEmail"
            }

        }
    }

    fun updateUserDataToFireStore(){

        val user = auth.uid

        val df = fStore.collection("Users").document(user!!)

        val map : HashMap<String, Any> = HashMap()

        val upName = name_input.text.toString().trim()
        //val upPassword = password_input.text.toString().trim()

        if(!upName.isNullOrEmpty())
        {
            map["name"] = upName

            df.update(map)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Successfully Updated User Name", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener{
                        Toast.makeText(context, it.toString() , Toast.LENGTH_LONG).show()
                    }
        }


//        if(!upPassword.isNullOrEmpty())
//        {
//            auth.currentUser!!.updatePassword(upPassword).addOnCompleteListener { task->
//                if(task.isSuccessful){
//                    Toast.makeText(context, "Successfully Updated", Toast.LENGTH_LONG).show()
//                }
//                else{
//                    Toast.makeText(
//                            context,
//                            task.exception!!.message.toString(),
//                            Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
//        }
//
//        if(!upPassword.isNullOrEmpty() && !upEmail.isNullOrEmpty())
//        {
//            signUserOut()
//        }

    }

    private fun loadProfileImage(){
        val profileRef = storageReference.reference.child("users/"+ auth.currentUser!!.uid+"/profile.jpg")
        profileRef.downloadUrl.addOnSuccessListener {
            Picasso.get().load(it)
                .transform(CropCircleTransformation())
                .into(profile_picture)
        }
    }

    fun selectImage(){

        val intent = Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){

            Log.e("Upload image", "Profile Picture was uploaded")

            imageUri = data.data

            profile_picture.setImageURI(imageUri)

            uploadImageBtn.visibility = View.VISIBLE

            Toast.makeText(context, "Click upload button to save selected image", Toast.LENGTH_LONG).show()
        }
    }

    fun uploadImage(){
        if(imageUri != null){
            uploadToFirebase(imageUri!!)
        }else{
            Toast.makeText(context, "Please Select Image", Toast.LENGTH_LONG).show()
        }
    }

    private fun uploadToFirebase(uri : Uri){

        val fileRef = storageReference.reference.child("users/"+ auth.currentUser!!.uid+"/profile.jpg")

        fileRef.putFile(uri)
                .addOnSuccessListener {
                    progressBar.visibility = View.INVISIBLE
                    uploadImageBtn.visibility = View.INVISIBLE

                    fileRef.downloadUrl.addOnSuccessListener {
                        Toast.makeText(context, "Uploaded Successfully", Toast.LENGTH_SHORT).show()
                        Picasso.get()
                            .load(uri)
                            .transform(CropCircleTransformation())
                            .into(profile_picture)
                    }
                }
                .addOnProgressListener {
                    progressBar.visibility = View.VISIBLE
                    uploadImageBtn.visibility = View.INVISIBLE
                }
                .addOnFailureListener{
                    progressBar.visibility = View.INVISIBLE
                    uploadImageBtn.visibility = View.INVISIBLE

                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
    }


    fun navigateToUpdateEmail(){
        findNavController().navigate(R.id.action_managerProfileFragment_to_updateEmailFragment)
    }
}