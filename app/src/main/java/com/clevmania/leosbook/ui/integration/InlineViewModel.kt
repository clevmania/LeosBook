package com.clevmania.leosbook.ui.integration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clevmania.leosbook.extension.toDefaultErrorMessage
import com.clevmania.leosbook.ui.integration.model.FWTransactionResponse
import com.clevmania.leosbook.utils.UiEventUtils
import kotlinx.coroutines.launch

class InlineViewModel(private val dataSource : TransactionDataSource) : ViewModel() {

    private val _progress = MutableLiveData<UiEventUtils<Boolean>>()
    val progress : LiveData<UiEventUtils<Boolean>> = _progress

    private val _error = MutableLiveData<UiEventUtils<String>>()
    val error : LiveData<UiEventUtils<String>> = _error

    private val _verificationResponse = MutableLiveData<UiEventUtils<FWTransactionResponse>>()
    val verificationResponse : LiveData<UiEventUtils<FWTransactionResponse>> = _verificationResponse

    fun verifyTransactionStatus(transactionId : String){
        viewModelScope.launch {

            _progress.value = UiEventUtils(true)
            try {
                val transactionStatus = dataSource.verify(transactionId)
                transactionStatus.data?.let {
                    _verificationResponse.value = UiEventUtils(it)
                }
            }catch (ex: Exception){
                _error.value = UiEventUtils(ex.toDefaultErrorMessage())
            }finally {
                _progress.value = UiEventUtils(false)
            }
        }
    }
}