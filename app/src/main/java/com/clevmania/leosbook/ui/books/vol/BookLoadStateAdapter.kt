package com.clevmania.leosbook.ui.books.vol

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.clevmania.leosbook.R
import kotlinx.android.synthetic.main.item_footer_load_state.view.*

/**
 * @author by Lawrence on 8/13/20.
 * for LeosBook
 */
class BookLoadStateAdapter(private val retry : () -> Unit)
    : LoadStateAdapter<BookLoadStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bindView(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder.create(parent, retry)
    }

    class LoadStateViewHolder(itemView: View, retry: () -> Unit):RecyclerView.ViewHolder(itemView){

        init {
            itemView.btnRetry.setOnClickListener { retry.invoke() }
        }

        fun bindView(loadState: LoadState) {
            if (loadState is LoadState.Error){
                itemView.tvErrorMsg.text = loadState.error.localizedMessage
            }
            itemView.pbLoader.isVisible = loadState is LoadState.Loading
            itemView.btnRetry.isVisible = loadState !is LoadState.Loading
            itemView.tvErrorMsg.isVisible = loadState !is LoadState.Loading
        }

        companion object{
            fun create(parent: ViewGroup, retry: () -> Unit): LoadStateViewHolder{
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_footer_load_state,parent,false)
                return LoadStateViewHolder(view,retry)
            }
        }
    }


}