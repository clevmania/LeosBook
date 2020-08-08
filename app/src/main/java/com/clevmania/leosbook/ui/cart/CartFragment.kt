package com.clevmania.leosbook.ui.cart

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.clevmania.leosbook.R
import com.clevmania.leosbook.data.Cart
import com.clevmania.leosbook.data.FirebaseUtils
import com.clevmania.leosbook.data.User
import com.clevmania.leosbook.extension.formatPrice
import com.clevmania.leosbook.extension.makeGone
import com.clevmania.leosbook.ui.checkout.CheckOutFragment
import com.clevmania.leosbook.utils.InjectorUtils
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.cart_fragment.*

class CartFragment : Fragment() {

    private var recyclerViewState : Parcelable? = null
    private lateinit var viewModel: CartViewModel
    private var costOfBooks : Double = 0.0
//    private val cartList = mutableListOf<Cart>()

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

    private var userEventListener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val user = snapshot.getValue(User::class.java)
            user?.let {
                CheckOutFragment.newInstance(it,costOfBooks)
                    .show(childFragmentManager,"Checkout")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            // Timber.log(error)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mbTotalPrice.setOnClickListener {
            FirebaseUtils.getUserDetails()?.addValueEventListener(userEventListener)
        }
//        viewModel.retrieveTotalCost()
//        rvCart.adapter = CartAdapter(cartList,cartDelegate)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(viewModel){
            allCartItems.observe(viewLifecycleOwner, Observer { uiEvent ->
                uiEvent.getContentIfNotHandled()?.let {
                    if(it.isEmpty())mbTotalPrice.makeGone()
//                    cartList.addAll(it)
                    viewModel.retrieveTotalCost()
                    rvCart.adapter = CartAdapter(it,cartDelegate)
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
                    costOfBooks = it * 20
                    mbTotalPrice.text = getString(R.string.pay_now, it.toInt().formatPrice())
                }
            })
        }
    }

}