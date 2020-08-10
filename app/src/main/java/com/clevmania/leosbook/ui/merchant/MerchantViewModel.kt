package com.clevmania.leosbook.ui.merchant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clevmania.leosbook.extension.toDefaultErrorMessage
import com.clevmania.leosbook.ui.merchant.model.TransactionResponse
import com.clevmania.leosbook.ui.merchant.model.TransactionResponseItem
import com.clevmania.leosbook.utils.UiEventUtils
import kotlinx.coroutines.launch

class MerchantViewModel(private val transactionDataSource: TransactionDataSource) : ViewModel() {
    private var _progress = MutableLiveData<UiEventUtils<Boolean>>()
    val progress : LiveData<UiEventUtils<Boolean>> = _progress

    private var _error = MutableLiveData<UiEventUtils<String>>()
    val error : LiveData<UiEventUtils<String>> = _error

    private var _transactions = MutableLiveData<UiEventUtils<List<TransactionResponseItem>>>()
    val transaction : LiveData<UiEventUtils<List<TransactionResponseItem>>> = _transactions

    init { getAllTransaction() }

    private fun getAllTransaction(){
        viewModelScope.launch {
            try {
                _progress.value = UiEventUtils(true)
                val allTrans = transactionDataSource.retrieveTransactions()

                allTrans.data?.let {
                    _transactions.value = UiEventUtils(it)
                }

            }catch ( ex: Exception){
                ex.printStackTrace()
                _error.value = UiEventUtils(ex.toDefaultErrorMessage())
            }finally {
                _progress.value = UiEventUtils(false)
            }
        }
    }
}