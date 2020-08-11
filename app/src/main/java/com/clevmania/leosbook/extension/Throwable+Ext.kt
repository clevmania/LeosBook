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
                    Constants.SERVER_ERROR
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
                        Constants.CLIENT_ERROR
                    }
                }
            }
        }
        is SocketTimeoutException -> {
            return Constants.SOCKET_TIMEOUT_ERROR
        }
        is UnknownHostException ->{
            return Constants.CONNECTION_ERROR
        }
        is ConnectException -> {
            return Constants.CONNECTION_ERROR
        }
        is IOException -> {
//            Timber.d(this)
            return Constants.IO_ERROR

        }
        else -> {
            "Unable to process request, ${this.localizedMessage}"
        }
    }
}