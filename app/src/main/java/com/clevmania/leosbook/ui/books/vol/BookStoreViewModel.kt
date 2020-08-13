package com.clevmania.leosbook.ui.books.vol

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.clevmania.leosbook.extension.toDefaultErrorMessage
import com.clevmania.leosbook.ui.books.vol.model.BookVolumeResponse
import com.clevmania.leosbook.ui.books.vol.model.Item
import com.clevmania.leosbook.utils.UiEventUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class BookStoreViewModel(private val dataSource: BookStoreDataSource) : ViewModel() {
    private val _progress = MutableLiveData<UiEventUtils<Boolean>>()
    val progress: LiveData<UiEventUtils<Boolean>> = _progress

    private val _error = MutableLiveData<UiEventUtils<String>>()
    val error: LiveData<UiEventUtils<String>> = _error

    private val _bookResponse = MutableLiveData<UiEventUtils<BookVolumeResponse>>()
    val bookResponse: LiveData<UiEventUtils<BookVolumeResponse>> = _bookResponse

    private var currentQueryValue : String? = null
    private var currentSearchResult : Flow<PagingData<Item>>? = null

//    init { getBooks("general") }

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

    fun getPaginatedBooks(bookCategory: String): Flow<PagingData<Item>>{
        val result = currentSearchResult
        if(bookCategory == currentQueryValue && result != null){
            return result
        }

        currentQueryValue = bookCategory
        val latestResult = dataSource.getBookStreamResult(bookCategory).cachedIn(viewModelScope)
        currentSearchResult = latestResult
        return latestResult
    }
}