package com.clevmania.leosbook.ui.merchant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import com.clevmania.leosbook.R
import com.clevmania.leosbook.ui.base.GroundFragment
import com.clevmania.leosbook.utils.InjectorUtils
import kotlinx.android.synthetic.main.merchant_fragment.*

class MerchantFragment : GroundFragment() {
    private var adapter = MerchantAdapter()

    companion object {
        fun newInstance() = MerchantFragment()
    }

    private lateinit var viewModel: MerchantViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            InjectorUtils.provideMerchantViewModelFactory()
        ).get(MerchantViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.merchant_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        btnRetry.setOnClickListener { adapter.retry() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(viewModel) {
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

            transaction.observe(viewLifecycleOwner, Observer { uiEvent ->
                uiEvent.getContentIfNotHandled()?.let {
                    adapter.submitData(lifecycle, it)
                }
            })
        }
    }

    private fun initAdapter() {
        rvTransactions.adapter = adapter
        adapter.addLoadStateListener { combinedLoadStates ->
            rvTransactions.isVisible = combinedLoadStates.source.refresh is LoadState.NotLoading
            toggleBlockingProgress(combinedLoadStates.source.refresh is LoadState.Loading)
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

}