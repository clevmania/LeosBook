package com.clevmania.leosbook.ui.checkout.model.request

import com.clevmania.leosbook.constants.Constants
import com.clevmania.leosbook.utils.UiUtils

data class BankTransferRequest(
    val amount: String,
    val currency: String = Constants.DEFAULT_CURRENCY,
    val duration: Int = Constants.DEFAULT_TRANSFER_DURATION,
    val email: String,
    val frequency: Int =Constants.DEFAULT_TRANSFER_FREQUENCY,
    val is_permanent: Boolean = true,
    val narration: String,
    val phone_number: String,
    val tx_ref: String = UiUtils.randomReferenceGenerator()
)