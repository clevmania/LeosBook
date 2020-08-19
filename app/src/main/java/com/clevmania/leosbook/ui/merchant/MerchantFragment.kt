package com.clevmania.leosbook.ui.merchant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.clevmania.leosbook.R
import com.clevmania.leosbook.extension.formatAmount
import com.clevmania.leosbook.extension.formatDate
import com.clevmania.leosbook.ui.base.GroundFragment
import com.clevmania.leosbook.ui.merchant.model.ClientTransactions
import com.clevmania.leosbook.utils.InjectorUtils
import kotlinx.android.synthetic.main.merchant_fragment.*

class MerchantFragment : GroundFragment() {
    private lateinit var adapter: MerchantAdapter
    private var transactionList = arrayListOf<ClientTransactions>()

    companion object {
        fun newInstance() = MerchantFragment()
    }

    private lateinit var viewModel: MerchantViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this,
            InjectorUtils.provideMerchantViewModelFactory()).get(MerchantViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.merchant_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MerchantAdapter(transactionList)
        rvTransactions.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(viewModel){
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
                    it.forEach {transItem ->
                        transactionList.add(
                            ClientTransactions(transItem.customer.name,transItem.flw_ref,
                                transItem.created_at.formatDate(),transItem.amount.formatAmount(),
                                transItem.payment_type.first().toString().toUpperCase(),
                                transItem.app_fee.formatAmount(),
                                transItem.amount_settled.formatAmount()
                            )
                        )
                    }
                    adapter.notifyDataSetChanged()
                }
            })
        }
    }

}