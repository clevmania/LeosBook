package com.clevmania.leosbook.ui.integration

import com.clevmania.leosbook.model.FWApiResponse
import com.clevmania.leosbook.ui.integration.model.FWTransactionResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author by Lawrence on 8/3/20.
 * for LeosBook
 */
interface TransactionVerificationService{
    @GET("v3/transactions/{id}/verify")
    suspend fun verifyTransaction(
        @Path("id") transId: String
    ): FWApiResponse<FWTransactionResponse>
}

interface TransactionDataSource{
    suspend fun verify (transactionId : String): FWApiResponse<FWTransactionResponse>
}

class TransactionRepository(
    private val apiService : TransactionVerificationService
): TransactionDataSource {
    override suspend fun verify(transactionId: String): FWApiResponse<FWTransactionResponse> {
        return apiService.verifyTransaction(transactionId)
    }

}