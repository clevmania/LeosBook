package com.clevmania.leosbook.ui.merchant

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.clevmania.leosbook.ui.merchant.model.TransactionResponseItem
import com.clevmania.leosbook.utils.UiEventUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MerchantViewModel(private val transactionDataSource: TransactionDataSource) : ViewModel() {
    private var _progress = MutableLiveData<UiEventUtils<Boolean>>()
    val progress : LiveData<UiEventUtils<Boolean>> = _progress

    private var _error = MutableLiveData<UiEventUtils<String>>()
    val error : LiveData<UiEventUtils<String>> = _error

    private var _transactions = MutableLiveData<UiEventUtils<PagingData<TransactionResponseItem>>>()
    val transaction : LiveData<UiEventUtils<PagingData<TransactionResponseItem>>> = _transactions

    init { getPaginatedTransactions() }

//    private fun getAllTransaction(){
//        viewModelScope.launch {
//            try {
//                _progress.value = UiEventUtils(true)
//                val allTrans = transactionDataSource.retrieveTransactions()
//
//                allTrans.data?.let {
//                    _transactions.value = UiEventUtils(it)
//                }
//
//            }catch ( ex: Exception){
//                ex.printStackTrace()
//                _error.value = UiEventUtils(ex.toDefaultErrorMessage())
//            }finally {
//                _progress.value = UiEventUtils(false)
//            }
//        }
//    }

    private fun getPaginatedTransactions() {
        viewModelScope.launch {
            val response = transactionDataSource.getTransactionStream().cachedIn(this)

            response.let {
                it.collectLatest {data ->
                    _transactions.value = UiEventUtils(data)
                }
            }
        }
    }
}