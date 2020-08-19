package com.clevmania.leosbook.ui.merchant

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.clevmania.leosbook.data.TransactionsPagingSource
import com.clevmania.leosbook.model.MerchantTransactions
import com.clevmania.leosbook.ui.merchant.model.TransactionResponseItem
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author by Lawrence on 8/9/20.
 * for LeosBook
 */
interface TransactionService {
    @GET("v3/transactions")
    suspend fun retrieveTransactions(): MerchantTransactions<List<TransactionResponseItem>>

    @GET("v3/transactions")
    suspend fun retrievePaginatedTransactions(
        @Query("page") page: Int
    ): MerchantTransactions<List<TransactionResponseItem>>
}

interface TransactionDataSource {
    suspend fun retrieveTransactions(): MerchantTransactions<List<TransactionResponseItem>>

    fun getTransactionStream(): Flow<PagingData<TransactionResponseItem>>
}

class MerchantTransactionRepository(private val apiService: TransactionService) :
    TransactionDataSource {
    override suspend fun retrieveTransactions(): MerchantTransactions<List<TransactionResponseItem>> {
        return apiService.retrieveTransactions()
    }

    override fun getTransactionStream(): Flow<PagingData<TransactionResponseItem>> {
        return Pager(config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
            enablePlaceholders = false
        ),
            pagingSourceFactory = { TransactionsPagingSource(apiService) }
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }
}