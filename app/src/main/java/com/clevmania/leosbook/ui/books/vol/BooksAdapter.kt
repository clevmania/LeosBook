package com.clevmania.leosbook.ui.books.vol

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.clevmania.leosbook.R
import com.clevmania.leosbook.extension.formatPrice
import com.clevmania.leosbook.extension.toSafeDirection
import com.clevmania.leosbook.ui.books.vol.model.Item
import com.clevmania.leosbook.utils.UiUtils
import kotlinx.android.synthetic.main.item_book.view.*

/**
 * @author by Lawrence on 8/2/20.
 * for LeosBook
 */

class BookAdapter : PagingDataAdapter<Item, BookAdapter.ViewHolder>(BOOK_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_book, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bindView(it)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(book: Item) {
            Glide.with(itemView.context)
                .load(book.volumeInfo.imageLinks.thumbnail)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(itemView.ivBookImg)

            itemView.tvBookAuthor.text = UiUtils.getAuthorsOrCategories(book.volumeInfo.authors)
            itemView.tvBookCategory.text =
                UiUtils.getAuthorsOrCategories(book.volumeInfo.categories)
            itemView.tvBookPrice.text = itemView.context.getString(
                R.string.price,
                book.volumeInfo.pageCount.formatPrice()
            )
            itemView.tvBookTitle.text = book.volumeInfo.title

            itemView.setOnClickListener {
                val action = BookStoreFragmentDirections
                    .actionBookStoreFragmentToBookDetailFragment(book.id)
                it.findNavController().toSafeDirection(action)
            }
        }
    }


    companion object {
        private val BOOK_COMPARATOR = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
                oldItem == newItem
        }
    }
}