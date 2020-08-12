package com.clevmania.leosbook.data

import androidx.paging.PagingSource
import com.clevmania.leosbook.ui.books.vol.BookStoreDataService
import com.clevmania.leosbook.ui.books.vol.model.BookVolumeResponse
import com.clevmania.leosbook.ui.books.vol.model.Item
import retrofit2.HttpException
import java.io.IOException

/**
 * @author by Lawrence on 8/13/20.
 * for LeosBook
 */

private const val GOOGLE_BOOK_STARTING_INDEX = 0

class BookStorePagingSource(
    private val apiService : BookStoreDataService, private val query : String
): PagingSource<Int, Item>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        val position = params.key ?: GOOGLE_BOOK_STARTING_INDEX

        return try {
            val response = apiService.fetchPaginatedBooks(query, position, params.loadSize)
            LoadResult.Page(
                response.items,
                prevKey = if(position == GOOGLE_BOOK_STARTING_INDEX) null else position -1,
                nextKey = if(response.items.isEmpty()) null else position + 1
            )
        }catch ( ex : IOException){
            LoadResult.Error(ex)
        }catch ( ex : HttpException){
            LoadResult.Error(ex)
        }
    }

}