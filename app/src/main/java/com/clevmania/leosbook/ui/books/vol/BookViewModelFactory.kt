package com.clevmania.leosbook.ui.books.vol

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @author by Lawrence on 8/2/20.
 * for LeosBook
 */
class BookViewModelFactory(private val dataSource: BookStoreDataSource)
    : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BookStoreViewModel(dataSource) as T
    }
}