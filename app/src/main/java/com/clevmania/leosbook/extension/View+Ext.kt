package com.clevmania.leosbook.extension

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE

/**
 * @author by Lawrence on 7/16/20.
 * for Uberx
 */
fun View.makeVisible(){ this.visibility = VISIBLE }

fun View.makeGone(){ this.visibility = GONE}

fun View.makeInVisible() { this.visibility = View.INVISIBLE}

fun Int.formatPrice(): String{
    return "NGN ${this * 20}"
}