package com.clevmania.leosbook.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clevmania.leosbook.data.local.Cart
import com.clevmania.leosbook.data.local.CartLocalDataSource
import com.clevmania.leosbook.data.FirebaseUtils
import com.clevmania.leosbook.extension.toDefaultErrorMessage
import com.clevmania.leosbook.utils.UiEventUtils
import kotlinx.coroutines.launch

class CartViewModel(private val cds: CartLocalDataSource) : ViewModel() {

    private val cartItems = mutableListOf<Cart>()

    private val _progress = MutableLiveData<UiEventUtils<Boolean>>()
    val progress : LiveData<UiEventUtils<Boolean>> = _progress

    private val _error = MutableLiveData<UiEventUtils<String>>()
    val error : LiveData<UiEventUtils<String>> = _error

    private val _allCartItems = MutableLiveData<UiEventUtils<List<Cart>>>()
    val allCartItems : LiveData<UiEventUtils<List<Cart>>> = _allCartItems

    private val _updatedQuantity = MutableLiveData<UiEventUtils<Unit>>()
    val updatedQuantity : LiveData<UiEventUtils<Unit>> = _updatedQuantity

    private val _totalCost = MutableLiveData<UiEventUtils<Double>>()
    val totalCost : LiveData<UiEventUtils<Double>> = _totalCost

    private val _deleteItem = MutableLiveData<UiEventUtils<Int>>()
    val deletedCartItem : LiveData<UiEventUtils<Int>> = _deleteItem

    init { retrieveItemsInCart() }

    private fun retrieveItemsInCart(){
        viewModelScope.launch {
            _progress.value = UiEventUtils(true)
            try {
                FirebaseUtils.getUID()?.let {
                    val itemsInCart = cds.getAllCartItem(it)
                    _allCartItems.value = UiEventUtils(itemsInCart)
                }
            }catch (ex : Exception){
                _error.value = UiEventUtils(ex.toDefaultErrorMessage())
            }finally {
                _progress.value = UiEventUtils(false)
            }
        }
    }

    fun updateBookQuantity(qty : Int, bookId: String){
        viewModelScope.launch {
            _progress.value = UiEventUtils(true)
            try {
                FirebaseUtils.getUID()?.let {
                    val res = cds.updateCartItemQuantity(qty,it, bookId)
                    _updatedQuantity.value = UiEventUtils(res)
                }
            }catch (ex : Exception){
                _error.value = UiEventUtils(ex.toDefaultErrorMessage())
            }finally {
                _progress.value = UiEventUtils(false)
            }
        }
    }

    fun retrieveTotalCost(){
        viewModelScope.launch {
            _progress.value = UiEventUtils(true)
            try {
                FirebaseUtils.getUID()?.let {
                    val cost = cds.retrievePriceInCart(it)
                    _totalCost.value = UiEventUtils(cost)
                }
            }catch (ex : Exception){
                _error.value = UiEventUtils(ex.toDefaultErrorMessage())
            }finally {
                _progress.value = UiEventUtils(false)
            }
        }
    }

    fun deleteCartItem(cartItem : Cart){
        viewModelScope.launch {
            _progress.value = UiEventUtils(true)
            try {
                val itemDeleted = cds.deleteCartItem(cartItem)
                _deleteItem.value = UiEventUtils(itemDeleted)
            }catch (ex : Exception){
                _error.value = UiEventUtils(ex.toDefaultErrorMessage())
            }finally {
                _progress.value = UiEventUtils(false)
            }
        }
    }

}