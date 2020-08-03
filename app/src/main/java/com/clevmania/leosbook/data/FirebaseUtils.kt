package com.clevmania.leosbook.data

import com.clevmania.leosbook.constants.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * @author by Lawrence on 8/1/20.
 * for LeosBook
 */
object FirebaseUtils {
    private fun getAuthInstance() = FirebaseAuth.getInstance()

    private fun getCurrentUser() = getAuthInstance().currentUser

    fun getUID() : String?{ return getCurrentUser()?.uid }

    private fun getDatabaseInstance() = FirebaseDatabase.getInstance()

    private fun getLeoBooksReference(): DatabaseReference {
        return getDatabaseInstance().getReference(Constants.LEOS_BOOKS)
    }

    private fun getUsersReference(): DatabaseReference {
        return getLeoBooksReference().child(Constants.LEOS_USER)
    }

    fun saveUserDetails(user : User, uid: String): Task<Void?>{
        return getUsersReference().child(uid).setValue(user)
    }

    fun signOut() { getAuthInstance().signOut() }
}

data class User(
    val firstName : String, val lastName : String,
    val mobile : String, val email: String)