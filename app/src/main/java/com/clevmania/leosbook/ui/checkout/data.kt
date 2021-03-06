package com.clevmania.leosbook.ui.checkout

import com.clevmania.leosbook.model.FWApiResponse
import com.clevmania.leosbook.ui.checkout.model.request.BankTransferRequest
import com.clevmania.leosbook.ui.checkout.model.request.UssdRequest
import com.clevmania.leosbook.ui.checkout.model.response.Meta
import com.clevmania.leosbook.ui.checkout.model.transfer.TransferMeta
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @author by Lawrence on 8/8/20.
 * for LeosBook
 */
interface UssdOrTransferService {
    @POST("v3/charges")
    suspend fun payWithUssd(
        @Query("type") type: String, @Body request : UssdRequest
    ): FWApiResponse<Meta>

    @POST("v3/charges?type=bank_transfer")
    suspend fun payWithBankTransfer(
       @Body request: BankTransferRequest
    ): FWApiResponse<TransferMeta>
}

interface UssdOrTransferDataSource {
    suspend fun payWithUssd(type: String= "ussd",request : UssdRequest): FWApiResponse<Meta>

    suspend fun payWithBankTransfer(request: BankTransferRequest
    ): FWApiResponse<TransferMeta>
}

class UssdOrTransferRepository(private val apiService: UssdOrTransferService) :
    UssdOrTransferDataSource {
    override suspend fun payWithUssd(
        type: String, request: UssdRequest
    ): FWApiResponse<Meta> {
        return apiService.payWithUssd(type,request)
    }

    override suspend fun payWithBankTransfer(request: BankTransferRequest
    ): FWApiResponse<TransferMeta> {
        return apiService.payWithBankTransfer(request)
    }
}