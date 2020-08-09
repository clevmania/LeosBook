package com.clevmania.leosbook.ui.merchant

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.clevmania.leosbook.R

class MerchantFragment : Fragment() {

    companion object {
        fun newInstance() = MerchantFragment()
    }

    private lateinit var viewModel: MerchantViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.merchant_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MerchantViewModel::class.java)
        // TODO: Use the ViewModel
    }

}