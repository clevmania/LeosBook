package com.clevmania.leosbook.ui.merchant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @author by Lawrence on 8/9/20.
 * for LeosBook
 */
class MerchantViewModelFactory(private val transactionDataSource: TransactionDataSource) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MerchantViewModel(transactionDataSource) as T
    }
}