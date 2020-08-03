package com.clevmania.leosbook.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.clevmania.leosbook.constants.Constants

/**
 * @author by Lawrence on 8/3/20.
 * for LeosBook
 */
@Database(entities = [Cart::class], version = 1, exportSchema = false)
abstract class CartDatabase : RoomDatabase() {
    abstract fun cartDao() : CartDao

    companion object{
        @Volatile private var instance : CartDatabase? = null

        fun getInstance(context: Context): CartDatabase {
            return instance ?: synchronized(this){
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    CartDatabase::class.java, Constants.DATABASE_NAME
                ).build().also { instance = it }
            }

        }
    }
}