package com.clevmania.leosbook.data

import androidx.room.*

/**
 * @author by Lawrence on 8/3/20.
 * for LeosBook
 */
@Dao
interface CartDao{
    @Query("Select * FROM Cart WHERE uid=:uid")
    suspend fun getAllCartItem(uid: String) : List<Cart>

    @Query("SELECT COUNT(*) FROM Cart WHERE uid=:uid")
    suspend fun countItemsInCart(uid: String): Int

    @Query("SELECT SUM(bookPrice * bookQuantity) FROM Cart WHERE uid=:uid")
    suspend fun retrievePriceInCart(uid: String) : Double

    @Query("SELECT * FROM Cart where bookId=:bookId AND uid=:uid")
    suspend fun getItemById(bookId : String, uid: String): Cart

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceCartItems(vararg carts: Cart)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCartItem(cart : Cart) : Int

    @Delete
    suspend fun deleteCartItem(cart: Cart): Int

    @Query("Delete FROM Cart where uid=:uid")
    suspend fun clearCart(uid: String)

    @Query("UPDATE Cart SET bookQuantity = :quantity where uid=:uid AND bookId=:bookId")
    suspend fun updateCartItemQuantity(quantity: Int, uid: String, bookId: String)

}