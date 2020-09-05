package com.clevmania.leosbook.ui.integration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.clevmania.leosbook.data.local.CartLocalDataSource

/**
 * @author by Lawrence on 8/3/20.
 * for LeosBook
 */
class InlineViewModelFactory(
    private val dataSource: TransactionDataSource, private val cds: CartLocalDataSource
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return InlineViewModel(dataSource, cds) as T
    }
}