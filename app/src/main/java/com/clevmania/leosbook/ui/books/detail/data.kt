package com.clevmania.leosbook.ui.books.detail

import com.clevmania.leosbook.ui.books.detail.model.BookDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author by Lawrence on 8/3/20.
 * for LeosBook
 */
interface BookDetailService{
    @GET("v1/volumes/{volumeId}")
    suspend fun retrieveBookDetails(@Path("volumeId") volId : String): BookDetailResponse
}

interface BookDetailDataSource{
    suspend fun retrieveBookDetails(volumeId : String): BookDetailResponse
}

class BookDetailRepository(val apiService: BookDetailService): BookDetailDataSource{
    override suspend fun retrieveBookDetails(volumeId: String): BookDetailResponse {
        return apiService.retrieveBookDetails(volumeId)
    }
}