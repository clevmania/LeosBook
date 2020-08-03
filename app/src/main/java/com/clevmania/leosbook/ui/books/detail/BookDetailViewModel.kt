package com.clevmania.leosbook.ui.books.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clevmania.leosbook.data.FirebaseUtils
import com.clevmania.leosbook.extension.toDefaultErrorMessage
import com.clevmania.leosbook.ui.books.detail.model.BookDetailResponse
import com.clevmania.leosbook.utils.UiEventUtils
import kotlinx.coroutines.launch
import java.lang.Exception

class BookDetailViewModel(private val bookDetailDataSource: BookDetailDataSource) : ViewModel() {
    private val _progress = MutableLiveData<UiEventUtils<Boolean>>()
    val progress : LiveData<UiEventUtils<Boolean>> = _progress

    private val _error = MutableLiveData<UiEventUtils<String>>()
    val error : LiveData<UiEventUtils<String>> = _error

    private val _bookDetail = MutableLiveData<UiEventUtils<BookDetailResponse>>()
    val bookDetail : LiveData<UiEventUtils<BookDetailResponse>> = _bookDetail

    private val _bookDescription = MutableLiveData<UiEventUtils<String>>()
    val bookDescription : LiveData<UiEventUtils<String>> = _bookDescription

    private val _itemInCart = MutableLiveData<UiEventUtils<Unit>>()
    val itemInCart : LiveData<UiEventUtils<Unit>> = _itemInCart

    private val _cartCounter = MutableLiveData<UiEventUtils<Int>>()
    val cartCounter : LiveData<UiEventUtils<Int>> = _cartCounter

    fun retrieveBookDetail(id : String){
        viewModelScope.launch {
            _progress.value = UiEventUtils(true)
            try {
                val response = bookDetailDataSource.retrieveBookDetails(id)
                response.let { _bookDetail.value = UiEventUtils(it) }

            }catch (ex : Exception){
                _error.value = UiEventUtils(ex.toDefaultErrorMessage())
            }finally {
                _progress.value = UiEventUtils(false)
            }
        }
    }

    fun saveDescription(desc : String){
        _bookDescription.value = UiEventUtils(desc)
    }

//    fun addBookToCart(cds : CartLocalDataSource, cart : Cart){
//        viewModelScope.launch {
//            try {
//                val insertSuccess = cds.insertOrReplaceCartItems(cart)
//                _itemInCart.value = UiEventUtils(insertSuccess)
//            }catch (ex : Exception){
//                _error.value = UiEventUtils(ex.toDefaultErrorMessage())
//            }
//        }
//    }
//
//    fun increaseCartCounter(cds: CartLocalDataSource){
//        viewModelScope.launch {
//            try {
//                val count = cds.countItemsInCart(FirebaseUtils.getUID())
//                _cartCounter.value = UiEventUtils(count)
//            }catch (ex : Exception){
//                _error.value = UiEventUtils(ex.toDefaultErrorMessage())
//            }
//        }
//    }
}