package com.clevmania.leosbook.ui.books.detail.description

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.clevmania.leosbook.R
import com.clevmania.leosbook.ui.books.detail.BookDetailViewModel
import kotlinx.android.synthetic.main.fragment_description.*


class DescriptionFragment : Fragment() {
    private lateinit var viewModel: BookDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_description, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(viewModel){
            bookDescription.observe(viewLifecycleOwner, Observer { uiEvent ->
                uiEvent.getContentIfNotHandled()?.let {
                    tvDescription.text = it
                }
            })
        }
    }

    companion object {
        fun newInstance(viewModel: BookDetailViewModel) =
            DescriptionFragment().apply { this.viewModel = viewModel }
    }
}