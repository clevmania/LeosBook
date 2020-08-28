package com.clevmania.leosbook.ui.books.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.clevmania.leosbook.data.local.CartLocalDataSource

/**
 * @author by Lawrence on 8/3/20.
 * for LeosBook
 */
class BookDetailViewModelFactory(private val dataSource: BookDetailDataSource,
                                 private val cds : CartLocalDataSource
)
    : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BookDetailViewModel(dataSource,cds) as T
    }
}