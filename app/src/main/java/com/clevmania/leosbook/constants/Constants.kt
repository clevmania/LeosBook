package com.clevmania.leosbook.constants

/**
 * @author by Lawrence on 8/2/20.
 * for LeosBook
 */
object Constants {
    const val API_TRANSACTION_SUCCESS = "successful"
    const val RC_SIGN_IN: Int = 1414
    const val LEOS_USER: String = "users"
    const val LEOS_BOOKS: String = "leobook"
    const val BOOK_QUANTITY: Int = 1
    const val alphaNumericCharacters = "0123456789QWERTYUIOPASDFGHJKLZXCVBNM"
    const val sizeOfRandomString = 12
    const val DATABASE_NAME: String = "shopping-cart.db"
    const val IO_ERROR = "An error occurred and we are unable to process request at the moment"
    const val CONNECTION_ERROR = "Unable to process request, please check your network connection and try again"
    const val SOCKET_TIMEOUT_ERROR = "Connection timed out, please check your network connection and try again"
    const val CLIENT_ERROR = "An error occurred while processing your request, please try again"
    const val SERVER_ERROR = "Unable to reach the server at the moment, please try again later"
}