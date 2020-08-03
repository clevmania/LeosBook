package com.clevmania.leosbook.ui.books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.clevmania.leosbook.R
import com.clevmania.leosbook.extension.afterTextChanged
import com.clevmania.leosbook.extension.formatPrice
import com.clevmania.leosbook.extension.makeVisible
import com.clevmania.leosbook.ui.GroundFragment
import com.clevmania.leosbook.ui.books.vol.BookAdapter
import com.clevmania.leosbook.utils.InjectorUtils
import kotlinx.android.synthetic.main.book_store_fragment.*

class BookStoreFragment : GroundFragment() {
    private lateinit var bookViewModel: BookStoreViewModel
    private lateinit var viewModelFactory: BookViewModelFactory
    private var bookStore = mutableListOf<BookStore>()
    private lateinit var adapter: BookAdapter

    companion object {
        fun newInstance() = BookStoreFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelFactory = InjectorUtils.provideViewModelFactory()
        bookViewModel =
            ViewModelProvider(this, viewModelFactory).get(BookStoreViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.book_store_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnArts.setOnClickListener { bookViewModel.getBooks(btnArts.text.toString()) }
        btnRomance.setOnClickListener { bookViewModel.getBooks(btnRomance.text.toString()) }
        btnSciFi.setOnClickListener { bookViewModel.getBooks(btnSciFi.text.toString()) }
        btnHistory.apply { setOnClickListener { bookViewModel.getBooks(text.toString()) } }
        adapter = BookAdapter(bookStore)
        rvBookList.adapter = adapter
        searchBooks()
        grpBookStore.makeVisible()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(bookViewModel) {
            progress.observe(viewLifecycleOwner, Observer { uiEvent ->
                uiEvent.getContentIfNotHandled()?.let {
                    toggleBlockingProgress(it)
                }
            })

            error.observe(viewLifecycleOwner, Observer { uiEvent ->
                uiEvent.getContentIfNotHandled()?.let {
                    showErrorDialog(it)
                }
            })

            bookResponse.observe(viewLifecycleOwner, Observer { uiEvent ->
                uiEvent.getContentIfNotHandled()?.let {
                    if (bookStore.isNotEmpty()) bookStore.clear()
                    it.items.forEach { item ->
                        bookStore.add(
                            BookStore(
                                item.volumeInfo.title,
                                item.volumeInfo.imageLinks.smallThumbnail,
                                "General",
                                item.volumeInfo.authors ?: listOf("N/A"),
                                item.volumeInfo.pageCount.formatPrice(),
                                item.id
                            )
                        )
                    }
                    adapter.notifyDataSetChanged()
                }
            })
        }
    }

    private fun searchBooks() {
        tieSearchBooks.afterTextChanged { query ->
            if (!query.isBlank()) {
                val searchResult = bookStore.filter {
                    it.title.toLowerCase().contains(query.toLowerCase())
                }
                adapter.updateItems(searchResult)
            } else {
                adapter.updateItems(bookStore)
            }
        }
    }

}

data class BookStore(
    val title: String, val thumbnail: String, val category: String,
    val authors: List<String>, val price: String, val volumeId: String
)