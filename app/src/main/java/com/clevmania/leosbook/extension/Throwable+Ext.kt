package com.clevmania.leosbook.extension

import android.util.Log
import com.clevmania.leosbook.constants.Constants
import com.google.gson.JsonParser
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author by Lawrence on 8/2/20.
 * for LeosBook
 */
fun Throwable.toDefaultErrorMessage(): String {
    return when (this) {
        is HttpException -> {
            return when (code()) {
                in 500..511 -> {
                    //server error
                    Constants.serverError
                }
                else -> {
                    //client error
                    return try {
                        val errorJsonString = response()?.errorBody()?.string()
                        Log.i("toDefaultErrorMessage","$errorJsonString")
                        JsonParser().parse(errorJsonString)
                            .asJsonObject["message"]
                            .asString
                    } catch (e: Exception) {
                        Constants.clientError
                    }
                }
            }
        }
        is SocketTimeoutException -> {
            return Constants.socketTimeoutError
        }
        is UnknownHostException ->{
            return Constants.connectionError
        }
        is ConnectException -> {
            return Constants.connectionError
        }
        is IOException -> {
//            Timber.d(this)
            return Constants.ioError

        }
        else -> {
            "Unable to process request, ${this.localizedMessage}"
        }
    }
}