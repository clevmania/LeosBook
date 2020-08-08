package com.clevmania.leosbook.ui.checkout

import com.clevmania.leosbook.model.FWApiResponse
import com.clevmania.leosbook.ui.checkout.model.request.BankTransferRequest
import com.clevmania.leosbook.ui.checkout.model.request.UssdRequest
import com.clevmania.leosbook.ui.checkout.model.response.Meta
import com.clevmania.leosbook.ui.checkout.model.response.UssdResponse
import com.clevmania.leosbook.ui.checkout.model.transfer.BankAuthorizationMeta
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @author by Lawrence on 8/8/20.
 * for LeosBook
 */
interface UssdOrTransferService {
    @POST("v3/charges")
    suspend fun payWithUssd(
        @Query("type") type: String, request : UssdRequest
    ): FWApiResponse<Meta>

    @POST("v3/charges?type=bank_transfer")
    suspend fun payWithBankTransfer(
        request: BankTransferRequest
    ): FWApiResponse<BankAuthorizationMeta>
}

interface UssdOrTransferDataSource {
    suspend fun payWithUssd(type: String= "ussd",request : UssdRequest): FWApiResponse<Meta>

    suspend fun payWithBankTransfer(request: BankTransferRequest
    ): FWApiResponse<BankAuthorizationMeta>
}

class UssdOrTransferRepository(private val apiService: UssdOrTransferService) :
    UssdOrTransferDataSource {
    override suspend fun payWithUssd(
        type: String, request: UssdRequest
    ): FWApiResponse<Meta> {
        return apiService.payWithUssd(type,request)
    }

    override suspend fun payWithBankTransfer(request: BankTransferRequest
    ): FWApiResponse<BankAuthorizationMeta> {
        return apiService.payWithBankTransfer(request)
    }
}