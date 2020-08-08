package com.clevmania.leosbook.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.clevmania.leosbook.R
import com.clevmania.leosbook.data.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_check_out.*

class CheckOutFragment : BottomSheetDialogFragment() {
    private lateinit var user : User
    private var amount: Double = 0.0

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
            findNavController().navigate(R.id.inlineFragment,bundle)
        }

        mbPayWithUssd
        mbBankTransfer
    }

    companion object {
        @JvmStatic
        fun newInstance(user : User, amount : Double) = CheckOutFragment().apply {
            this.user = user
            this.amount = amount
        }
    }
}