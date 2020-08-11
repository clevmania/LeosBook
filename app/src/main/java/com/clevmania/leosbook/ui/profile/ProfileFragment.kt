package com.clevmania.leosbook.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.clevmania.leosbook.R
import com.clevmania.leosbook.data.FirebaseUtils
import com.clevmania.leosbook.data.User
import com.clevmania.leosbook.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.sign_up_fragment.*
import kotlinx.android.synthetic.main.sign_up_fragment.tilEmail
import kotlinx.android.synthetic.main.sign_up_fragment.tilFirstName
import kotlinx.android.synthetic.main.sign_up_fragment.tilLastName
import kotlinx.android.synthetic.main.sign_up_fragment.tilMobile

class ProfileFragment : BottomSheetDialogFragment() {
    private lateinit var auth : FirebaseAuth
    private var costOfBooks: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mbUpdateProfile.setOnClickListener { createOrUpdateProfile() }
    }

    private fun createOrUpdateProfile(){
        try {
            val firstName = tilFirstName.validate(ValidationType.NAME, getString(R.string.first_name))
            val lastName = tilLastName.validate(ValidationType.NAME, getString(R.string.last_name))
            val email = tilEmail.validate(ValidationType.EMAIL,getString(R.string.email))
            val mobile = tilMobile.validate(ValidationType.PHONE, getString(R.string.mobile))
            ProgressDialogUtils.getInstance()
                .showProgressDialog(requireContext(),"Saving Profile",true)

            val newUser = User(firstName, lastName, mobile, email)
            auth.uid?.let {
                FirebaseUtils.saveUserDetails(newUser,it).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        dismiss()
                        val bundle = bundleOf("userInfo" to newUser, "amount" to costOfBooks)
                        findNavController().navigate(R.id.action_cartFragment_to_checkOutFragment, bundle)
                    }else{
                        // Failed to save
                    }
                }

            }
        }catch (ex : ValidationException){
            ex.printStackTrace()
        }finally {
            ProgressDialogUtils.getInstance().dismissProgressDialog()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(amount: Double) = ProfileFragment().apply {
            this.costOfBooks = amount
        }
    }
}