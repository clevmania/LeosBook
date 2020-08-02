package com.clevmania.leosbook.ui.onboard

import android.content.SharedPreferences
import androidx.core.content.edit

class OnBoardingManager(private val sharedPref: SharedPreferences) {

    fun setAppsFirstLaunch(isFirstRun: Boolean) {
        sharedPref.edit(commit = true) { putBoolean("AppsFirstRun", isFirstRun) }
    }

    fun isFirstLaunch(): Boolean {
        return sharedPref.getBoolean("AppsFirstRun", true)
    }
}