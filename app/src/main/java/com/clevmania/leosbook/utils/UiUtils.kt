package com.clevmania.leosbook.utils

import com.clevmania.leosbook.constants.Constants
import java.util.*

/**
 * @author by Lawrence on 8/3/20.
 * for LeosBook
 */
object UiUtils{
    fun randomReferenceGenerator(): String {
        val sb = StringBuilder(Constants.sizeOfRandomString)
        for (i in 0 until Constants.sizeOfRandomString)
            sb.append(
                Constants.alphaNumericCharacters[Random().nextInt(
                Constants.alphaNumericCharacters.length)])
        return "TXF-LB-${sb}"
    }
}