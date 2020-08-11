package com.clevmania.leosbook.ui.books.detail.model

data class BookDetailResponse(
    val accessInfo: AccessInfo,
    val etag: String,
    val id: String,
    val kind: String,
    val layerInfo: LayerInfo,
    val saleInfo: SaleInfo,
    val selfLink: String,
    val volumeInfo: VolumeInfo
)