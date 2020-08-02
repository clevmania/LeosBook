package com.clevmania.leosbook.ui.books

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.clevmania.leosbook.R

class BookStoreFragment : Fragment() {

    companion object {
        fun newInstance() = BookStoreFragment()
    }

    private lateinit var viewModel: BookStoreViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.book_store_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BookStoreViewModel::class.java)
        // TODO: Use the ViewModel
    }

}