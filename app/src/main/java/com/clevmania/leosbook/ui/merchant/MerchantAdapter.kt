package com.clevmania.leosbook.ui.merchant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.clevmania.leosbook.R
import com.clevmania.leosbook.extension.formatAmount
import com.clevmania.leosbook.extension.formatDate
import com.clevmania.leosbook.ui.merchant.model.TransactionResponseItem
import kotlinx.android.synthetic.main.item_transaction.view.*

/**
 * @author by Lawrence on 8/10/20.
 * for LeosBook
 */
class MerchantAdapter : PagingDataAdapter<TransactionResponseItem,
        MerchantAdapter.ViewHolder>(TRANSACTIONS_COMPARATOR) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(it: TransactionResponseItem) {
            itemView.tvCustomerName.text = it.customer.name
            itemView.tvReference.text = it.flw_ref
            itemView.tvPaymentType.text = it.payment_type.first().toString().toUpperCase()
            itemView.tvTransactionDate.text = it.created_at.formatDate()
            itemView.tvAmount.text = it.amount.formatAmount()
            itemView.tvNetAmount.text = it.amount_settled.formatAmount()
            itemView.tvProcessingFee.text = it.app_fee.formatAmount()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_transaction, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bindView(it)
        }
    }

    companion object {
        private val TRANSACTIONS_COMPARATOR =
            object : DiffUtil.ItemCallback<TransactionResponseItem>() {
                override fun areItemsTheSame(
                    oldItem: TransactionResponseItem, newItem: TransactionResponseItem
                ): Boolean = oldItem.flw_ref == newItem.flw_ref

                override fun areContentsTheSame(
                    oldItem: TransactionResponseItem,
                    newItem: TransactionResponseItem
                ): Boolean = oldItem == newItem
            }
    }
}