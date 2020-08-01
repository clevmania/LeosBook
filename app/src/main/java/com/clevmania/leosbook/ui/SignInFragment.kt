package com.clevmania.leosbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.clevmania.leosbook.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.sign_in_fragment.*

class SignInFragment : AuthFragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_in_fragment, container, false)
    }

    private fun signUpUser(email: String, password: String) {
        tieEmail.text.toString()
        tiePassword.text.toString()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // navigate home
                    auth.currentUser
                } else {
                    // Auth Failed
                }
            }
    }

}