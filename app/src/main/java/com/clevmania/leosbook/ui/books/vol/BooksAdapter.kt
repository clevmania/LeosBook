package com.clevmania.leosbook.ui.books.vol

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.clevmania.leosbook.R
import com.clevmania.leosbook.ui.books.BookStoreFragment
import kotlinx.android.synthetic.main.item_book.view.*

/**
 * @author by Lawrence on 8/2/20.
 * for LeosBook
 */

class BookAdapter(private val booksList: List<BookStoreFragment.BookStore>) :
    RecyclerView.Adapter<BookAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_book, parent, false)
        )
    }

    override fun getItemCount() = booksList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(booksList[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(book: BookStoreFragment.BookStore) {
            Glide.with(itemView.context)
                .load(book.thumbnail)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(itemView.ivBookImg)
            itemView.tvBookAuthor.text = book.authors.first()
            itemView.tvBookCategory.text = book.category
            itemView.tvBookPrice.text = book.price
            itemView.tvBookTitle.text = book.title

            itemView.setOnClickListener {
//                val action = BookStoreFragmentDirections
//                    .actionBookStoreFragmentToBookDetailFragment(book.volumeId)
//                it.findNavController().navigate(action)
            }
        }
    }
}