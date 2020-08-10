package com.clevmania.leosbook.ui.merchant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.clevmania.leosbook.R
import com.clevmania.leosbook.extension.formatDate
import com.clevmania.leosbook.ui.merchant.model.ClientTransactions
import kotlinx.android.synthetic.main.item_transaction.view.*

/**
 * @author by Lawrence on 8/10/20.
 * for LeosBook
 */
class MerchantAdapter(private val transactionList : List<ClientTransactions>)
    : RecyclerView.Adapter<MerchantAdapter.ViewHolder>() {


    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        fun bindView(trans: ClientTransactions) {
            itemView.tvCustomerName.text = trans.customerName
            itemView.tvReference.text = trans.ref
            itemView.tvPaymentType.text = trans.paymentType
            itemView.tvTransactionDate.text = trans.date
            itemView.tvAmount.text = trans.amount
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent,false)
        )
    }

    override fun getItemCount() = transactionList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(transactionList[position])
    }
}