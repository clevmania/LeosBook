package com.clevmania.leosbook.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.clevmania.leosbook.data.CartLocalDataSource

/**
 * @author by Lawrence on 8/3/20.
 * for LeosBook
 */
class CartViewModelFactory(private val dataSource: CartLocalDataSource)
    : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CartViewModel(dataSource) as T
    }
}