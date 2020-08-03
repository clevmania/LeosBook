package com.clevmania.leosbook.utils

import android.content.Context
import android.content.SharedPreferences
import com.clevmania.leosbook.BookStoreService
import com.clevmania.leosbook.ui.books.BookStoreDataService
import com.clevmania.leosbook.ui.books.BookStoreRepository
import com.clevmania.leosbook.ui.books.BookViewModelFactory

/**
 * @author by Lawrence on 8/1/20.
 * for LeosBook
 */
object InjectorUtils {
    fun provideSharedPreference(context: Context): SharedPreferences{
        return context.getSharedPreferences("FirstInstallation", Context.MODE_PRIVATE)
    }

    private fun <T : Any>provideBookService(cls : Class<out T>): T{
        return BookStoreService().create(cls)
    }

    fun provideBookStoreRepository(): BookStoreRepository{
        return BookStoreRepository(provideBookService(BookStoreDataService::class.java))
    }

    fun provideViewModelFactory(): BookViewModelFactory{
        return BookViewModelFactory(provideBookStoreRepository())
    }
}