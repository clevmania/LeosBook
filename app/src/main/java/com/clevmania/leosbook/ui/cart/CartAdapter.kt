package com.clevmania.leosbook.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.clevmania.leosbook.R
import com.clevmania.leosbook.data.local.Cart
import com.clevmania.leosbook.extension.formatPrice
import kotlinx.android.synthetic.main.item_cart.view.*

/**
 * @author by Lawrence on 8/3/20.
 * for LeosBook
 */
class CartAdapter(private val cartList : List<Cart>, private val delegate : CartEventListener):
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_cart, parent, false)
        )
    }

    override fun getItemCount() = cartList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(cartList[position], delegate)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(cartItem: Cart, delegate: CartEventListener) {
            Glide.with(itemView.context)
                .load(cartItem.bookImage)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .transform(CenterCrop(), RoundedCorners(16))
                .into(itemView.ivThumbnail)
//            itemView.tvBookAuthor.text = cartItem.bookAuthor
            itemView.tvBookPrice.text = cartItem.bookPrice.formatPrice()
            itemView.tvBookTitle.text = cartItem.bookName

            itemView.ivDelete.setOnClickListener {
                delegate.onRemoveItemClicked(cartItem)
            }

            itemView.npQuantityPicker.apply {
                value = cartItem.bookQuantity
                setOnValueChangedListener { _, _, newVal ->
                    delegate.onQuantityChanged(newVal,cartItem.bookId)
                }
            }

            itemView.ivFavourite.setOnClickListener {
                // To be implemented soon
            }
        }
    }
}

interface CartEventListener{
    fun onQuantityChanged(quantity : Int, bookId: String)
    fun onRemoveItemClicked(cart: Cart)
    fun onSelectFavourite()
}