package com.clevmania.leosbook.ui.integration.model

data class Card(
    val country: String,
    val expiry: String,
    val first_6digits: String,
    val issuer: String,
    val last_4digits: String,
    val token: String,
    val type: String
)