package com.clevmania.leosbook.ui.checkout.model.request

import com.clevmania.leosbook.constants.Constants
import com.clevmania.leosbook.utils.UiUtils

data class UssdRequest(
    val account_bank: String,
    val amount: String,
    val currency: String = Constants.DEFAULT_CURRENCY,
    val email: String,
    val fullname: String,
    val phone_number: String,
    val tx_ref: String = UiUtils.randomReferenceGenerator()
)