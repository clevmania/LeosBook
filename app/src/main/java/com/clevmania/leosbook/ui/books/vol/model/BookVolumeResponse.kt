package com.clevmania.leosbook.ui.books.vol.model

data class BookVolumeResponse(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
)