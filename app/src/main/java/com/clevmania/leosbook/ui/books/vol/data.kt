package com.clevmania.leosbook.ui.books.vol

import com.clevmania.leosbook.ui.books.vol.model.BookVolumeResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author by Lawrence on 8/2/20.
 * for LeosBook
 */
interface BookStoreDataService {
    @GET("v1/volumes")
    suspend fun fetchBooks(@Query("q") query: String): BookVolumeResponse
}

interface BookStoreDataSource {
    suspend fun fetchBooks(bookCategory: String): BookVolumeResponse
}

class BookStoreRepository(private val apiService: BookStoreDataService) :
    BookStoreDataSource {
    override suspend fun fetchBooks(bookCategory: String): BookVolumeResponse {
        return apiService.fetchBooks(bookCategory)
    }
}