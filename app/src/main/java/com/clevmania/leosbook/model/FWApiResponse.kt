package com.clevmania.leosbook.model

/**
 * @author by Lawrence on 8/3/20.
 * for LeosBook
 */
data class FWApiResponse<out T>(val status : String?, //val meta: Meta?,
                                val message: String?, val data: T?)