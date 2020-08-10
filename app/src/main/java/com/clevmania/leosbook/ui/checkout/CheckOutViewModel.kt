package com.clevmania.leosbook.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clevmania.leosbook.extension.toDefaultErrorMessage
import com.clevmania.leosbook.ui.checkout.model.request.BankTransferRequest
import com.clevmania.leosbook.ui.checkout.model.request.UssdRequest
import com.clevmania.leosbook.ui.checkout.model.response.Meta
import com.clevmania.leosbook.ui.checkout.model.transfer.BankAuthorizationMeta
import com.clevmania.leosbook.utils.UiEventUtils
import kotlinx.coroutines.launch

/**
 * @author by Lawrence on 8/8/20.
 * for LeosBook
 */
class CheckOutViewModel(private val dataSource: UssdOrTransferDataSource) : ViewModel() {
    private val _progress = MutableLiveData<UiEventUtils<Boolean>>()
    val progress: LiveData<UiEventUtils<Boolean>> = _progress

    private val _error = MutableLiveData<UiEventUtils<String>>()
    val error: LiveData<UiEventUtils<String>> = _error

    private val _ussdMeta = MutableLiveData<UiEventUtils<Meta>>()
    val ussdMeta: LiveData<UiEventUtils<Meta>> = _ussdMeta

    private val _transferMeta = MutableLiveData<UiEventUtils<BankAuthorizationMeta>>()
    val transferMeta: LiveData<UiEventUtils<BankAuthorizationMeta>> = _transferMeta

    fun payViaUSSD(request: UssdRequest) {
        viewModelScope.launch {

            _progress.value = UiEventUtils(true)
            try {
                val ussdMeta = dataSource.payWithUssd(request = request)
                ussdMeta.meta?.let {
                    _ussdMeta.value = UiEventUtils(it)
                }
            } catch (ex: Exception) {
                _error.value = UiEventUtils(ex.toDefaultErrorMessage())
            } finally {
                _progress.value = UiEventUtils(false)
            }
        }
    }

    fun payViaBankTransfer(request: BankTransferRequest) {
        viewModelScope.launch {
            _progress.value = UiEventUtils(true)

            try {
                val meta = dataSource.payWithBankTransfer(request)
                meta.meta?.let {
                    _transferMeta.value = UiEventUtils(it)
                }
            } catch (ex: Exception) {
                _error.value = UiEventUtils(ex.toDefaultErrorMessage())
            } finally {
                _progress.value = UiEventUtils(false)
            }
        }
    }
}