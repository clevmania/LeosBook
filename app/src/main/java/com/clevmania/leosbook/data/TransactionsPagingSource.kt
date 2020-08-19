package com.clevmania.leosbook.data

import androidx.paging.PagingSource
import com.clevmania.leosbook.ui.merchant.TransactionService
import com.clevmania.leosbook.ui.merchant.model.TransactionResponseItem
import retrofit2.HttpException
import java.io.IOException

/**
 * @author by Lawrence on 8/19/20.
 * for LeosBook
 */
const val FLUTTER_TRANSACTION_STARTING_PAGE_INDEX = 1

class TransactionsPagingSource(
    private val apiService: TransactionService
): PagingSource<Int, TransactionResponseItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TransactionResponseItem> {
        val position = params.key ?: FLUTTER_TRANSACTION_STARTING_PAGE_INDEX

        return try {
            val response = apiService.retrievePaginatedTransactions(
                FLUTTER_TRANSACTION_STARTING_PAGE_INDEX)
            LoadResult.Page(
                response.data!!,
                prevKey = if(position == FLUTTER_TRANSACTION_STARTING_PAGE_INDEX) null else position -1,
                nextKey = if(response.data.isEmpty()) null else position + 1
            )
        }catch ( ex : IOException){
            LoadResult.Error(ex)
        }catch ( ex : HttpException){
            LoadResult.Error(ex)
        }
    }
}