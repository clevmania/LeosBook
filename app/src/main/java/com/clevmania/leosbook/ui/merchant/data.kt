package com.clevmania.leosbook.ui.merchant

import com.clevmania.leosbook.model.FWApiResponse
import com.clevmania.leosbook.model.MerchantTransactions
import com.clevmania.leosbook.ui.merchant.model.TransactionResponse
import com.clevmania.leosbook.ui.merchant.model.TransactionResponseItem
import retrofit2.http.GET

/**
 * @author by Lawrence on 8/9/20.
 * for LeosBook
 */
interface TransactionService{
    @GET("v3/transactions")
    suspend fun retrieveTransactions(): MerchantTransactions<List<TransactionResponseItem>>
}

interface TransactionDataSource{
    suspend fun retrieveTransactions(): MerchantTransactions<List<TransactionResponseItem>>
}

class MerchantTransactionRepository(private val apiService: TransactionService): TransactionDataSource{
    override suspend fun retrieveTransactions(): MerchantTransactions<List<TransactionResponseItem>> {
        return apiService.retrieveTransactions()
    }
}