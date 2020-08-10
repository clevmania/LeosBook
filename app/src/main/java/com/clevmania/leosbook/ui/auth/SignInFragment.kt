package com.clevmania.leosbook.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.clevmania.leosbook.R
import com.clevmania.leosbook.constants.Constants
import com.clevmania.leosbook.extension.toDefaultErrorMessage
import com.clevmania.leosbook.ui.AuthFragment
import com.clevmania.leosbook.utils.ValidationException
import com.clevmania.leosbook.utils.ValidationType
import com.clevmania.leosbook.utils.validate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.sign_in_fragment.*

class SignInFragment : AuthFragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
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
            startActivityForResult(googleSignInClient.signInIntent, Constants.RC_SIGN_IN)
        }
        mbSignIn.setOnClickListener { signUpUser() }
        tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        tvMerchants.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_merchantFragment) }
    }

    private fun signUpUser() {
        try {
            val email = tilEmail.validate(ValidationType.EMAIL, getString(R.string.email))
            val password = tilPassword.validate(
                ValidationType.PASSWORD, getString(R.string.password)
            )
            toggleBlockingProgress(true)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseGoogleHandshake(account.idToken!!)
            } catch (e: ApiException) {
                e.message?.let {
                    showErrorDialog(it, "Google Sign In Failed")
                }
            }
        }
    }

    private fun firebaseGoogleHandshake(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // prompt user to create and save their details
                    findNavController().navigate(R.id.action_signInFragment_to_bookStoreFragment)
                } else {
                    longSnackBar("Authentication Failed.")
                }
            }
    }

}