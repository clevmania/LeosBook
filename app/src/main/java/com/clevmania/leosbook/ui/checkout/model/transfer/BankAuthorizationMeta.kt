package com.clevmania.leosbook.ui.checkout.model.transfer

data class BankAuthorizationMeta(
    val account_expiration: String,
    val mode: String,
    val transfer_account: String,
    val transfer_amount: Int,
    val transfer_bank: String,
    val transfer_note: String,
    val transfer_reference: String
)

data class TransferMeta(
    val authorization: BankAuthorizationMeta
)