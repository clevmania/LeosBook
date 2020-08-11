package com.clevmania.leosbook.data

/**
 * @author by Lawrence on 8/3/20.
 * for LeosBook
 */
interface CartDataSource {
    suspend fun getAllCartItem(uid: String) : List<Cart>

    suspend fun countItemsInCart(uid: String): Int

    suspend fun retrievePriceInCart(uid: String) : Double

    suspend fun getItemById(bookId : String, uid: String): Cart

    suspend fun insertOrReplaceCartItems(vararg carts: Cart)

    suspend fun updateCartItem(cart : Cart) : Int

    suspend fun deleteCartItem(cart: Cart): Int

    suspend fun clearCart(uid: String)

    suspend fun updateCartItemQuantity(quantity: Int, uid: String, bookId: String)

}