package com.clevmania.leosbook.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @author by Lawrence on 8/8/20.
 * for LeosBook
 */
class CheckOutViewModelFactory(private val dataSource: UssdOrTransferDataSource)
    : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CheckOutViewModel(dataSource) as T
    }
}