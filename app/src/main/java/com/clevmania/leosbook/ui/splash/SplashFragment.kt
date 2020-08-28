package com.clevmania.leosbook.ui.splash

import androidx.navigation.fragment.findNavController
import com.clevmania.leosbook.R
import com.clevmania.leosbook.ui.base.AuthFragment
import com.clevmania.leosbook.ui.onboard.OnBoardingManager
import com.clevmania.leosbook.utils.InjectorUtils

/**
 * @author by Lawrence on 8/1/20.
 * for LeosBook
 */
class SplashFragment : AuthFragment() {
    private val sharedPreferences by lazy { InjectorUtils.provideSharedPreference(requireContext()) }

    override fun onStart() {
        super.onStart()
        val boardingManager = OnBoardingManager(sharedPreferences)
        if (boardingManager.isFirstLaunch()) {
            boardingManager.setAppsFirstLaunch(false)
            findNavController().navigate(R.id.action_splashFragment_to_onBoardFragment)
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
        }
    }
}