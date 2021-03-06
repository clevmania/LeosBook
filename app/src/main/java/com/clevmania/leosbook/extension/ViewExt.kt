package com.clevmania.leosbook.extension

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * @author by Lawrence on 7/16/20.
 * for Uberx
 */
fun View.makeVisible(){ this.visibility = VISIBLE }

fun View.makeGone(){ this.visibility = GONE}

fun View.makeInVisible() { this.visibility = View.INVISIBLE}

fun Int.formatPrice(): String{
    return "${this * 20}"
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun EditText.onActionDone(actionToPerform: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            actionToPerform.invoke()
            true
        }else{
            false
        }
    }
}

fun RecyclerView.hideOrShowFloatingActionButton(fab : FloatingActionButton){
    this.addOnScrollListener(object : RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0 && fab.isShown) {
                fab.hide()
            } else if (dy < 0 && !fab.isShown) {
                fab.show()
            }
        }
    })
}