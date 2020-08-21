package com.clevmania.leosbook.data.local

import com.clevmania.leosbook.data.local.Cart
import com.clevmania.leosbook.data.local.CartDao
import com.clevmania.leosbook.data.local.CartDataSource

/**
 * @author by Lawrence on 8/3/20.
 * for LeosBook
 */
class CartLocalDataSource(private val cartDao: CartDao):
    CartDataSource {
    override suspend fun getAllCartItem(uid: String) = cartDao.getAllCartItem(uid)

    override suspend fun countItemsInCart(uid: String) = cartDao.countItemsInCart(uid)

    override suspend fun retrievePriceInCart(uid: String): Double {
        return cartDao.retrievePriceInCart(uid)
    }

    override suspend fun getItemById(bookId: String, uid: String): Cart {
        return cartDao.getItemById(bookId,uid)
    }

    override suspend fun insertOrReplaceCartItems(vararg carts: Cart) {
        cartDao.insertOrReplaceCartItems(*carts)
    }

    override suspend fun updateCartItem(cart: Cart): Int {
        return cartDao.updateCartItem(cart)
    }

    override suspend fun deleteCartItem(cart: Cart): Int {
        return cartDao.deleteCartItem(cart)
    }

    override suspend fun clearCart(uid: String) {
        cartDao.clearCart(uid)
    }

    override suspend fun updateCartItemQuantity(quantity: Int, uid: String, bookId: String) {
        cartDao.updateCartItemQuantity(quantity, uid, bookId)
    }
}