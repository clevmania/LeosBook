package com.clevmania.leosbook.ui.splash

import androidx.navigation.fragment.findNavController
import com.clevmania.leosbook.R
import com.clevmania.leosbook.ui.AuthFragment

/**
 * @author by Lawrence on 8/1/20.
 * for LeosBook
 */
class SplashFragment: AuthFragment() {
    override fun onStart() {
        super.onStart()
        findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
    }
}