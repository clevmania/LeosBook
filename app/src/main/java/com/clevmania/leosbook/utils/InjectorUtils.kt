package com.clevmania.leosbook.utils

import android.content.Context
import android.content.SharedPreferences
import com.clevmania.leosbook.api.BookStoreService
import com.clevmania.leosbook.api.FWApiService
import com.clevmania.leosbook.data.CartDao
import com.clevmania.leosbook.data.CartDatabase
import com.clevmania.leosbook.data.CartLocalDataSource
import com.clevmania.leosbook.ui.books.vol.BookStoreDataService
import com.clevmania.leosbook.ui.books.vol.BookStoreRepository
import com.clevmania.leosbook.ui.books.vol.BookViewModelFactory
import com.clevmania.leosbook.ui.books.detail.BookDetailRepository
import com.clevmania.leosbook.ui.books.detail.BookDetailService
import com.clevmania.leosbook.ui.books.detail.BookDetailViewModelFactory
import com.clevmania.leosbook.ui.cart.CartViewModelFactory
import com.clevmania.leosbook.ui.checkout.CheckOutViewModelFactory
import com.clevmania.leosbook.ui.checkout.UssdOrTransferRepository
import com.clevmania.leosbook.ui.checkout.UssdOrTransferService
import com.clevmania.leosbook.ui.integration.InlineViewModelFactory
import com.clevmania.leosbook.ui.integration.TransactionRepository
import com.clevmania.leosbook.ui.integration.TransactionVerificationService
import com.clevmania.leosbook.ui.merchant.MerchantTransactionRepository
import com.clevmania.leosbook.ui.merchant.MerchantViewModelFactory
import com.clevmania.leosbook.ui.merchant.TransactionService

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

    private fun <T : Any>provideFWService(cls : Class<out T>): T{
        return FWApiService().create(cls)
    }

    private fun provideBookStoreRepository(): BookStoreRepository {
        return BookStoreRepository(
            provideBookService(BookStoreDataService::class.java)
        )
    }

    fun provideViewModelFactory(): BookViewModelFactory {
        return BookViewModelFactory(
            provideBookStoreRepository()
        )
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

    private fun provideCartDataSource(context: Context): CartLocalDataSource {
        return CartLocalDataSource(provideDao(context))
    }

    fun provideCartViewModelFactory(context: Context): CartViewModelFactory {
        return CartViewModelFactory(provideCartDataSource(context))
    }

    private fun provideFWInlineRepository(): TransactionRepository{
        return TransactionRepository(provideFWService(TransactionVerificationService::class.java))
    }

    fun provideInlineViewModelFactory(context: Context): InlineViewModelFactory{
        return InlineViewModelFactory(provideFWInlineRepository(), provideCartDataSource(context))
    }

    private fun provideBankTransferRepository(): UssdOrTransferRepository{
        return UssdOrTransferRepository(provideFWService(UssdOrTransferService::class.java))
    }

    fun provideUssdOrTransferViewModelFactory(): CheckOutViewModelFactory{
        return CheckOutViewModelFactory(provideBankTransferRepository())
    }

    private fun provideMerchantRepository(): MerchantTransactionRepository{
        return MerchantTransactionRepository(provideFWService(TransactionService::class.java))
    }

    fun provideMerchantViewModelFactory(): MerchantViewModelFactory{
        return MerchantViewModelFactory(provideMerchantRepository())
    }

}