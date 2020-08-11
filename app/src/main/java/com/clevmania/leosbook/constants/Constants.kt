package com.clevmania.leosbook.constants

import com.clevmania.leosbook.ui.checkout.SupportedBankList

/**
 * @author by Lawrence on 8/2/20.
 * for LeosBook
 */
object Constants {
    const val CHROME_PACKAGE: String = "com.android.chrome"
    const val DEFAULT_CURRENCY: String = "NGN"
    const val DEFAULT_TRANSFER_DURATION: Int = 3
    const val DEFAULT_TRANSFER_FREQUENCY: Int = 14
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

    fun getSupportedBanks(): List<SupportedBankList>{
        return listOf(
            SupportedBankList("Choose Bank","000"),
            SupportedBankList("Fidelity Bank","070"),
            SupportedBankList("Guaranty Trust Bank","058"),
            SupportedBankList("Keystone Bank","082"),
            SupportedBankList("Sterling Bank","232"),
            SupportedBankList("United Bank for Africa","033"),
            SupportedBankList("Unity Bank","215"),
            SupportedBankList("Zenith Bank","057")
        )
    }
}