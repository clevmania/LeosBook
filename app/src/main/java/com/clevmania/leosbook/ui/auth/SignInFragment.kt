package com.clevmania.leosbook.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.clevmania.leosbook.R
import com.clevmania.leosbook.extension.toDefaultErrorMessage
import com.clevmania.leosbook.ui.AuthFragment
import com.clevmania.leosbook.utils.ValidationException
import com.clevmania.leosbook.utils.ValidationType
import com.clevmania.leosbook.utils.validate
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mbSignInWithGoogle.setOnClickListener {
//            findNavController().navigate(R.id.action_signInFragment_to_bookStoreFragment)
        }
        mbSignIn.setOnClickListener { signUpUser() }
        tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

    private fun signUpUser() {
        toggleBlockingProgress(true)
        try {
            val email = tilEmail.validate(ValidationType.EMAIL, getString(R.string.email))
            val password = tilPassword.validate(
                ValidationType.PASSWORD, getString(R.string.password)
            )
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        findNavController()
                            .navigate(R.id.action_signInFragment_to_bookStoreFragment)
                    } else {
                        // Auth Failed
                        longSnackBar("Wrong email or password. Try Again!")
                    }
                }
        } catch (ex: ValidationException) {
            ex.printStackTrace()
        }catch (ex : Exception){
            showErrorDialog(ex.toDefaultErrorMessage())
        } finally {
            toggleBlockingProgress(false)
        }
    }

}