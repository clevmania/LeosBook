package com.clevmania.leosbook.ui.integration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @author by Lawrence on 8/3/20.
 * for LeosBook
 */
class InlineViewModelFactory(private val dataSource: TransactionDataSource)
    : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return InlineViewModel(dataSource) as T
    }
}