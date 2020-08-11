package com.clevmania.leosbook.utils

/**
 * @author by Lawrence on 8/2/20.
 * for LeosBook
 */
open class UiEventUtils<out T>(private val content : T) {
    var hasBeenHandled = false
        private set

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled){
            null
        }else{
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent() : T = content
}