package com.clevmania.leosbook.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.clevmania.leosbook.R
import com.clevmania.leosbook.ui.AuthFragment
import com.clevmania.leosbook.utils.ValidationException
import com.clevmania.leosbook.utils.ValidationType
import com.clevmania.leosbook.utils.validate
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.sign_up_fragment.*

class SignUpFragment : AuthFragment() {
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_up_fragment, container, false)
    }

    fun signUserUp(){
        try {
            val email = tilEmail.validate(ValidationType.EMAIL,getString(R.string.email))
            val mobile = tilMobile.validate(ValidationType.PHONE, getString(R.string.mobile))
            val password = tilPassword.validate(ValidationType.PASSWORD, getString(R.string.password))

            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener {task ->
                    if(task.isSuccessful){
                        // Navigate home
                        auth.currentUser
                        // save details to db
                    }else{
                        // Auth Failed
                    }
                }
        }catch (ex : ValidationException){
            ex.printStackTrace()
        }

    }

}