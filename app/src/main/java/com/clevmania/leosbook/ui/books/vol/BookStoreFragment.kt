package com.clevmania.leosbook.ui.books.vol

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.filter
import com.clevmania.leosbook.R
import com.clevmania.leosbook.extension.afterTextChanged
import com.clevmania.leosbook.ui.base.GroundFragment
import com.clevmania.leosbook.ui.books.vol.model.Item
import com.clevmania.leosbook.utils.InjectorUtils
import kotlinx.android.synthetic.main.book_store_fragment.*
import kotlinx.android.synthetic.main.book_store_fragment.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class BookStoreFragment : GroundFragment() {
    private lateinit var bookViewModel: BookStoreViewModel
    private var bookStore: PagingData<Item>? = null
    private var adapter = BookAdapter()
    private var bookJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bookViewModel = ViewModelProvider(
            this, InjectorUtils.provideViewModelFactory()
        ).get(BookStoreViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.book_store_fragment, container, false)
    }

    private val clickListener = View.OnClickListener { v ->
        when (v?.id) {
            R.id.btnArts -> {
                getBookInCategory(v.btnArts.text.toString())
            }
            R.id.btnRomance -> {
                getBookInCategory(v.btnRomance.text.toString())
            }
            R.id.btnSciFi -> {
                getBookInCategory(v.btnSciFi.text.toString())
            }
            R.id.btnHistory -> {
                getBookInCategory(v.btnHistory.text.toString())
            }
            R.id.btnAll -> {
                getBookInCategory(getString(R.string.general))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnArts.setOnClickListener(clickListener)
        btnRomance.setOnClickListener(clickListener)
        btnSciFi.setOnClickListener(clickListener)
        btnHistory.setOnClickListener(clickListener)
        btnAll.setOnClickListener(clickListener)
        btnRetry.setOnClickListener { adapter.retry() }

        scrollToTopPositionOnNewSearch()
//        grpBookStore.makeVisible()
        searchBooks()
        initAdapter()
    }

    private fun initAdapter() {
        rvBookList.adapter = adapter.withLoadStateHeaderAndFooter(
            header = BookLoadStateAdapter { adapter.retry() },
            footer = BookLoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { combinedLoadStates ->
            rvBookList.isVisible = combinedLoadStates.source.refresh is LoadState.NotLoading
            progressBar.isVisible = combinedLoadStates.source.refresh is LoadState.Loading
            btnRetry.isVisible = combinedLoadStates.source.refresh is LoadState.Error


            // For any other error
            val errorState = combinedLoadStates.source.append as? LoadState.Error
                ?: combinedLoadStates.source.prepend as? LoadState.Error
                ?: combinedLoadStates.append as? LoadState.Error
                ?: combinedLoadStates.prepend as? LoadState.Error

            errorState?.let {
                showErrorDialog(
                    "\uD83D\uDE28 Wooops ${it.error}",
                    "Something went wrong"
                )
            }
        }
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
                    bookStore = it
                    adapter.submitData(lifecycle, it)
                }
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
                                    .joinToString(", ").contains(query, true)
                    }
                    adapter.submitData(lifecycle, searchResult)
                } else {
                    adapter.submitData(lifecycle, it)
                }
            }
        }
    }

    private fun getBookInCategory(name: String) {
        bookJob?.cancel()
        bookJob = lifecycleScope.launch {
            bookViewModel.getPaginatedBooks(name).collectLatest {
                bookStore = it
                adapter.submitData(it)
            }
        }
    }

    private fun scrollToTopPositionOnNewSearch() {
        lifecycleScope.launch {
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { rvBookList.scrollToPosition(0) }
        }
    }

}