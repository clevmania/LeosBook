package com.clevmania.leosbook.extension

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

/**
 * @author by Lawrence on 8/28/20.
 * for LeosBook
 */
fun NavController.safelyNavigateTo(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navExtras: Navigator.Extras? = null
) {
    val action = currentDestination?.getAction(resId) ?: graph.getAction(resId)
    if (action != null && currentDestination?.id != action.destinationId) {
        navigate(resId, args, navOptions, navExtras)
    }
}

fun NavController.toSafeDirection(direction : NavDirections){
    safelyNavigateTo(direction.actionId,direction.arguments)
}