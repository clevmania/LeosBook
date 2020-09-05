package com.clevmania.leosbook.extension

import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author by Lawrence on 8/10/20.
 * for LeosBook
 */
fun String.formatDate() : String{
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
    val desiredFormat = SimpleDateFormat("MMM d", Locale.getDefault())
    return desiredFormat.format(format.parse(this))
}

fun Int.formatAmount(): String{
    val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
    formatter.applyPattern("#,###,###,###.00")
    return when (this){
        0 -> "\u20a60.00"
        else -> "\u20a6${formatter.format(this)}"
    }
}

fun Double.formatAmount(): String{
    val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
    formatter.applyPattern("#,###,###,###.00")
    return when (this){
        0.0 -> "\u20a60.00"
        else -> "\u20a6${formatter.format(this)}"
    }
}