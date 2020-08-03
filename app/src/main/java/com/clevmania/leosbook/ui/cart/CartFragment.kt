package com.clevmania.leosbook.ui.cart

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.clevmania.leosbook.R
import com.clevmania.leosbook.extension.formatPrice
import com.clevmania.leosbook.utils.InjectorUtils
import kotlinx.android.synthetic.main.cart_fragment.*

class CartFragment : Fragment() {

    private var recyclerViewState : Parcelable? = null
    private lateinit var viewModel: CartViewModel

    private var cartDelegate = object : CartEventListener{
        override fun onQuantityChanged(quantity: Int, bookId: String) {
            viewModel.updateBookQuantity(quantity,bookId)
            recyclerViewState = rvCart.layoutManager?.onSaveInstanceState()
        }

        override fun onRemoveItemClicked(id: String) {
            TODO("Not yet implemented")
        }

        override fun onSelectFavourite() {
            TODO("Not yet implemented")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = InjectorUtils.provideCartViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this,
            viewModelFactory).get(CartViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cart_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(viewModel){
            allCartItems.observe(viewLifecycleOwner, Observer { uiEvent ->
                uiEvent.getContentIfNotHandled()?.let {
                    val adapter = CartAdapter(it,cartDelegate)
                    rvCart.adapter = adapter
                }
            })

            updatedQuantity.observe(viewLifecycleOwner, Observer { uiEvent->
                uiEvent.getContentIfNotHandled()?.let {
                    rvCart.layoutManager?.onRestoreInstanceState(recyclerViewState)
                    viewModel.retrieveTotalCost()
                }
            })

            totalCost.observe(viewLifecycleOwner, Observer { uiEvent ->
                uiEvent.getContentIfNotHandled()?.let {
                    Toast.makeText(requireContext(),
                        it.toInt().formatPrice(),Toast.LENGTH_SHORT).show()
                    mbTotalPrice.text = getString(R.string.pay_now, it.toInt().formatPrice())
                }
            })
        }
    }

}