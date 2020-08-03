package com.clevmania.leosbook.utils

import android.content.Context
import android.content.SharedPreferences
import com.clevmania.leosbook.BookStoreService
import com.clevmania.leosbook.data.CartDao
import com.clevmania.leosbook.data.CartDatabase
import com.clevmania.leosbook.data.CartLocalDataSource
import com.clevmania.leosbook.ui.books.BookStoreDataService
import com.clevmania.leosbook.ui.books.BookStoreRepository
import com.clevmania.leosbook.ui.books.BookViewModelFactory
import com.clevmania.leosbook.ui.books.detail.BookDetailRepository
import com.clevmania.leosbook.ui.books.detail.BookDetailService
import com.clevmania.leosbook.ui.books.detail.BookDetailViewModelFactory
import com.clevmania.leosbook.ui.cart.CartViewModelFactory

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

    private fun provideBookStoreRepository(): BookStoreRepository{
        return BookStoreRepository(provideBookService(BookStoreDataService::class.java))
    }

    fun provideViewModelFactory(): BookViewModelFactory{
        return BookViewModelFactory(provideBookStoreRepository())
    }

    private fun provideBookDetailRepository(): BookDetailRepository{
        return BookDetailRepository(provideBookService(BookDetailService::class.java))
    }

    fun provideBookDetailViewModelFactory(context: Context): BookDetailViewModelFactory{
        return BookDetailViewModelFactory(
            provideBookDetailRepository(), provideCartDataSource(context))
    }

    private fun provideDatabase(context: Context): CartDatabase {
        return CartDatabase.getInstance(context)
    }

    private fun provideDao(context: Context): CartDao {
        return provideDatabase(context).cartDao()
    }

    fun provideCartDataSource(context: Context): CartLocalDataSource {
        return CartLocalDataSource(provideDao(context))
    }

    fun provideCartViewModelFactory(context: Context): CartViewModelFactory {
        return CartViewModelFactory(provideCartDataSource(context))
    }
}