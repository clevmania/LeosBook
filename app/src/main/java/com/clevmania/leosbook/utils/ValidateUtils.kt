package com.clevmania.leosbook.utils

import android.text.TextUtils
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

/**
 * @author by Lawrence on 8/1/20.
 * for LeosBook
 */

class ValidationException (message:String)  : Exception(message)

enum class ValidationType {
    EMAIL, PHONE, PASSWORD, REQUIRED, NAME
}

@Throws(ValidationException::class)
fun TextInputLayout.validate(validationType: ValidationType,
                             label: String, length : Int?=null): String{
    val value = this.editText?.text.toString()

    // Validate empty field
    if (value.trim().isBlank()) {
        this.error = "$label is required"
        this.isErrorEnabled = true
        throw  ValidationException("$label is required")
    }

    // Validate the length of input
    if (length != null && value.length != length) {
        this.error = "$label should be $length characters long"
        this.isErrorEnabled = true
        throw  ValidationException("$label should be $length characters long")
    }

    // Proceed to validate
    when (validationType) {
        ValidationType.EMAIL -> {
            if (!isValidEmail(value)) {
                this.error = "$label is invalid"
                this.isErrorEnabled = true
                throw  ValidationException("$label is invalid")
            }
        }
        ValidationType.PASSWORD -> {
            val minEightXter = "^.{8,}$"
            if (!Pattern.compile(minEightXter).matcher(value).find()) {
                this.error = "Password must be at least 8 characters"
                this.isErrorEnabled = true
                throw  ValidationException("Password must be at least 8 characters ")
            }
        }
        ValidationType.PHONE -> {
            val mobilePattern = "^([0])([7-9])([0-1])\\d{8}$"
            if(!Pattern.compile(mobilePattern).matcher(value).matches()){
                this.error = "Incorrect Mobile Number"
                this.isErrorEnabled = true
                throw ValidationException("Incorrect Mobile Number")
            }
        }
        ValidationType.NAME -> {
            if(TextUtils.isDigitsOnly(value)){
                this.error = "$label cannot be a number"
                this.isErrorEnabled = true
                throw ValidationException("Cannot be a number")
            }
        }
        else -> {
            //No type is provided, please proceed
        }
    }

    this.error = " "
    this.isErrorEnabled = false
    return value.trim()
}

private fun isValidEmail(email: String): Boolean {
    return email.isNotEmpty()
            && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
