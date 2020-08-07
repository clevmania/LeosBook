package com.clevmania.leosbook.ui.integration.model

data class FWTransactionResponse(
    val account_id: Int,
    val amount: Int,
    val amount_settled: Int,
    val app_fee: Int,
    val auth_model: String,
    val card: Card?,
    val charged_amount: Int,
    val created_at: String,
    val currency: String,
    val customer: Customer,
    val device_fingerprint: String,
    val flw_ref: String,
    val id: Int,
    val ip: String,
    val merchant_fee: Int,
    val narration: String,
    val payment_type: String,
    val processor_response: String,
    val status: String,
    val tx_ref: String
)