package com.clevmania.leosbook.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author by Lawrence on 8/3/20.
 * for LeosBook
 */
@Entity
data class Cart (
    @PrimaryKey()
    val bookId : String,
    val bookName : String,
    val bookImage : String,
    val bookPrice : Int,
    val bookQuantity : Int,
    val uid : String
//    val mobile : String,
)