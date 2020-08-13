package com.clevmania.leosbook.ui.books.vol

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.filter
import com.clevmania.leosbook.R
import com.clevmania.leosbook.extension.afterTextChanged
import com.clevmania.leosbook.extension.formatPrice
import com.clevmania.leosbook.extension.makeVisible
import com.clevmania.leosbook.ui.base.GroundFragment
import com.clevmania.leosbook.ui.books.vol.model.Item
import com.clevmania.leosbook.utils.InjectorUtils
import kotlinx.android.synthetic.main.book_store_fragment.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class BookStoreFragment : GroundFragment() {
    private lateinit var bookViewModel: BookStoreViewModel
    private lateinit var viewModelFactory: BookViewModelFactory
    private var bookStore : PagingData<Item>? = null
    private  var adapter = BookAdapter()
    private var bookJob : Job? = null

    companion object {
        fun newInstance() =
            BookStoreFragment()
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
        btnArts.setOnClickListener { getBookInCategory(btnArts.text.toString()) }
        btnRomance.setOnClickListener { getBookInCategory(btnRomance.text.toString()) }
        btnSciFi.setOnClickListener { getBookInCategory(btnSciFi.text.toString()) }
        btnHistory.apply { setOnClickListener { getBookInCategory(text.toString()) } }

        rvBookList.adapter = adapter
        getBookInCategory("general")
        scrollToTopPositionOnNewSearch()
        grpBookStore.makeVisible()
        searchBooks()

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
//                uiEvent.getContentIfNotHandled()?.let {
//                    bookStore.clear()
//                    it.items.forEach { item ->
//                        bookStore.add(
//                            BookStore(
//                                item.volumeInfo.title,
//                                item.volumeInfo.imageLinks.smallThumbnail,
//                                "General",
//                                item.volumeInfo.authors ?: listOf("N/A"),
//                                getString(R.string.price,item.volumeInfo.pageCount.formatPrice()),
//                                item.id
//                            )
//                        )
//                    }
//                    adapter.updateItems(bookStore)
//                }
            })
        }
    }

    private fun searchBooks() {
        tieSearchBooks.afterTextChanged { query ->
            bookStore?.let {
                if (!query.isBlank()) {
                    val searchResult = it.filter { item ->
                        item.volumeInfo.title.contains(query, ignoreCase = true) ||
                                item.volumeInfo.authors != null &&
                                item.volumeInfo.authors
                                    .joinToString(", ").contains(query, true)}
                    adapter.submitData(lifecycle, searchResult)
                } else {
                    adapter.submitData(lifecycle,it)
                }
            }
        }
    }

    private fun getBookInCategory(name : String){
        bookJob?.cancel()
        bookJob = lifecycleScope.launch {
            bookViewModel.getPaginatedBooks(name).collectLatest {
                bookStore = it
                adapter.submitData(it)
            }
        }
    }

    private fun scrollToTopPositionOnNewSearch(){
        lifecycleScope.launch {
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { rvBookList.scrollToPosition(0) }
        }
    }


}

data class BookStore(
    val title: String, val thumbnail: String, val category: String,
    val authors: List<String>, val price: String, val volumeId: String
)