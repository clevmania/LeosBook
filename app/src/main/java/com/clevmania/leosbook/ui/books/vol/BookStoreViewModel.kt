package com.clevmania.leosbook.ui.books.vol

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clevmania.leosbook.extension.toDefaultErrorMessage
import com.clevmania.leosbook.ui.books.vol.model.BookVolumeResponse
import com.clevmania.leosbook.utils.UiEventUtils
import kotlinx.coroutines.launch

class BookStoreViewModel(private val dataSource: BookStoreDataSource) : ViewModel() {
    private val _progress = MutableLiveData<UiEventUtils<Boolean>>()
    val progress: LiveData<UiEventUtils<Boolean>> = _progress

    private val _error = MutableLiveData<UiEventUtils<String>>()
    val error: LiveData<UiEventUtils<String>> = _error

    private val _bookResponse = MutableLiveData<UiEventUtils<BookVolumeResponse>>()
    val bookResponse: LiveData<UiEventUtils<BookVolumeResponse>> = _bookResponse

    init { getBooks("general") }

    fun getBooks(bookCategory: String) {
        viewModelScope.launch {
            _progress.value = UiEventUtils(content = true)

            try {
                val response = dataSource.fetchBooks(bookCategory)
                response.let {
                    _bookResponse.value = UiEventUtils(it)
                }
            } catch (ex: Exception) {
                _error.value = UiEventUtils(ex.toDefaultErrorMessage())
            } finally {
                _progress.value = UiEventUtils(content = false)
            }
        }
    }
}