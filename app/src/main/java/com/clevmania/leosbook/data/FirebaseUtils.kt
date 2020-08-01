package com.clevmania.leosbook.data

import com.google.firebase.auth.FirebaseAuth

/**
 * @author by Lawrence on 8/1/20.
 * for LeosBook
 */
object FirebaseUtils {
    private fun getAuthInstance() = FirebaseAuth.getInstance()

    private fun getCurrentUser() = getAuthInstance().currentUser

    fun getUID() : String?{ return getCurrentUser()?.uid }

    fun signOut() { getAuthInstance().signOut() }
}