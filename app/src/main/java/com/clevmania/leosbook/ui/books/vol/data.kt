package com.clevmania.leosbook.ui.books.vol

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.clevmania.leosbook.data.BookStorePagingSource
import com.clevmania.leosbook.ui.books.vol.model.BookVolumeResponse
import com.clevmania.leosbook.ui.books.vol.model.Item
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author by Lawrence on 8/2/20.
 * for LeosBook
 */
interface BookStoreDataService {
    @GET("v1/volumes")
    suspend fun fetchBooks(@Query("q") query: String): BookVolumeResponse

    @GET("v1/volumes")
    suspend fun fetchPaginatedBooks(
        @Query("q") query: String,
        @Query("startIndex") page : Int,
        @Query("maxResults") maxResult : Int
    ): BookVolumeResponse
}

interface BookStoreDataSource {
    suspend fun fetchBooks(bookCategory: String): BookVolumeResponse

    suspend fun fetchPaginatedBooks(query: String, page: Int, maxResult : Int): BookVolumeResponse

    fun getBookStreamResult(query: String) : Flow<PagingData<Item>>
}

class BookStoreRepository(private val apiService: BookStoreDataService) :
    BookStoreDataSource {
    override suspend fun fetchBooks(bookCategory: String): BookVolumeResponse {
        return apiService.fetchBooks(bookCategory)
    }

    override suspend fun fetchPaginatedBooks(
        query: String,
        page: Int,
        maxResult: Int
    ): BookVolumeResponse {
        return apiService.fetchPaginatedBooks(query,page,maxResult)
    }

    override fun getBookStreamResult(query: String): Flow<PagingData<Item>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGING_SIZE,enablePlaceholders = false),
            pagingSourceFactory = { BookStorePagingSource(apiService,query) }
        ).flow
    }

    companion object{
        const val NETWORK_PAGING_SIZE = 10
    }
}