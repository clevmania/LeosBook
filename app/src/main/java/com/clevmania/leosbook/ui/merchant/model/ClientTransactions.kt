package com.clevmania.leosbook.ui.merchant.model

/**
 * @author by Lawrence on 8/10/20.
 * for LeosBook
 */
data class ClientTransactions(
    val customerName : String, val ref : String,
    val date: String, val amount : String,
    val paymentType : String, val fee: String, val settlement: String)