package com.clevmania.leosbook.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.clevmania.leosbook.R
import com.clevmania.leosbook.constants.Constants
import com.clevmania.leosbook.data.User
import com.clevmania.leosbook.extension.makeVisible
import com.clevmania.leosbook.ui.checkout.model.request.BankTransferRequest
import com.clevmania.leosbook.ui.checkout.model.request.UssdRequest
import com.clevmania.leosbook.utils.InjectorUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_check_out.*

class CheckOutFragment : BottomSheetDialogFragment() {
    private lateinit var user: User
    private var amount: Double = 0.0

    private val bankList by lazy { Constants.getSupportedBanks() }
    private lateinit var viewModel: CheckOutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this,
            InjectorUtils.provideUssdOrTransferViewModelFactory())
            .get(CheckOutViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_out, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mbPayWithCard.setOnClickListener {
            val bundle = bundleOf(
                "userName" to "${user.firstName} ${user.lastName}",
                "userMobile" to user.mobile,
                "userMail" to user.email,
                "totalCost" to amount
            )
            findNavController().navigate(R.id.inlineFragment, bundle)
        }

        loadBanks()
        mbPayWithUssd.setOnClickListener { spSupportedBanks.makeVisible() }
        mbBankTransfer.setOnClickListener { payViaBankTransfer() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(viewModel) {
            progress.observe(viewLifecycleOwner, Observer { uiEvent ->
                uiEvent.getContentIfNotHandled()?.let {
//                    toggleBlockingProgress(it)
                }
            })

            error.observe(viewLifecycleOwner, Observer { uiEvent ->
                uiEvent.getContentIfNotHandled()?.let {
//                    showErrorDialog(it)
                }
            })

            ussdMeta.observe(viewLifecycleOwner, Observer { uiEvent ->
                uiEvent.getContentIfNotHandled()?.let {
                    // show meta info view
                }
            })

            transferMeta.observe(viewLifecycleOwner, Observer { uiEvent ->
                uiEvent.getContentIfNotHandled()?.let {
                    // show bank info view
                }
            })
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(user: User, amount: Double) = CheckOutFragment().apply {
            this.user = user
            this.amount = amount
        }
    }

    private val bankSelectionListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (position != 0) {
                val bankCode = bankList[position].code
                val request = UssdRequest(
                    bankCode, amount.toString(),
                    email = user.email, fullname = "${user.firstName} ${user.lastName}",
                    phone_number = user.mobile
                )
                viewModel.payViaUSSD(request)
            }
        }
    }

    private fun payViaBankTransfer(){
        val request = BankTransferRequest(amount.toString(),
            email = user.email,narration = "", phone_number = user.mobile)
        viewModel.payViaBankTransfer(request)
    }

    private fun loadBanks(){
        val adapter = ArrayAdapter(requireContext(), R.layout.item_spinner,bankList.map { it.name })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spSupportedBanks.adapter = adapter
        spSupportedBanks.onItemSelectedListener = bankSelectionListener
    }
}

data class SupportedBankList(var name: String, var code: String)