package com.clevmania.leosbook.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * @author by Lawrence on 8/1/20.
 * for LeosBook
 */
object InjectorUtils {
    fun provideSharedPreference(context: Context): SharedPreferences{
        return context.getSharedPreferences("FirstInstallation", Context.MODE_PRIVATE)
    }
}