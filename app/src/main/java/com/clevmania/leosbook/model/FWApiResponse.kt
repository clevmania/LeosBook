package com.clevmania.leosbook.model

import com.clevmania.leosbook.ui.checkout.model.response.Meta

/**
 * @author by Lawrence on 8/3/20.
 * for LeosBook
 */
data class FWApiResponse<out T>(val status : String?, val meta: T?,
                                val message: String?, val data: T?)

data class MerchantTransactions<out T>(val status : String?, //val meta: T?,
                                          val message: String?, val data: T?)