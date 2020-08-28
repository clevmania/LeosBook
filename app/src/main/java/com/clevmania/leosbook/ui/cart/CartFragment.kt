package com.clevmania.leosbook.ui.cart

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.clevmania.leosbook.R
import com.clevmania.leosbook.data.local.Cart
import com.clevmania.leosbook.data.FirebaseUtils
import com.clevmania.leosbook.data.User
import com.clevmania.leosbook.extension.formatPrice
import com.clevmania.leosbook.extension.makeGone
import com.clevmania.leosbook.extension.makeVisible
import com.clevmania.leosbook.ui.base.TopLevelFragment
import com.clevmania.leosbook.ui.profile.ProfileFragment
import com.clevmania.leosbook.utils.InjectorUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.cart_fragment.*

class CartFragment : TopLevelFragment() {

    private var recyclerViewState: Parcelable? = null
    private lateinit var viewModel: CartViewModel
    private var costOfBooks: Double = 0.0
    private val cartList = arrayListOf<Cart>()
    private lateinit var adapter: CartAdapter
    private var removeIndex : Int? = null

    private var cartDelegate = object : CartEventListener {
        override fun onQuantityChanged(quantity: Int, bookId: String) {
            viewModel.updateBookQuantity(quantity, bookId)
            recyclerViewState = rvCart.layoutManager?.onSaveInstanceState()
        }

        override fun onRemoveItemClicked(cart: Cart) {
            viewModel.deleteCartItem(cart)
            removeIndex = cartList.indexOf(cart)
        }

        override fun onSelectFavourite() {
            TODO("Not yet implemented")
        }

    }

    private var userEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            toggleBlockingProgress(false)
            val user = snapshot.getValue(User::class.java)
            if(user == null){
                ProfileFragment.newInstance(costOfBooks)
                    .show(childFragmentManager,getString(R.string.create_update_profile))
            }else{
                val bundle = bundleOf("userInfo" to user, "amount" to costOfBooks)
                findNavController().navigate(R.id.action_cartFragment_to_checkOutFragment, bundle)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            // Timber.log(error)
            toggleBlockingProgress(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = InjectorUtils.provideCartViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this,
            viewModelFactory).get(CartViewModel::class.java)
        adapter = CartAdapter(cartList,cartDelegate)
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
            toggleBlockingProgress(true)
            FirebaseUtils.getUserDetails()?.addValueEventListener(userEventListener)
        }
        viewModel.retrieveTotalCost()
        rvCart.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(viewModel) {
            allCartItems.observe(viewLifecycleOwner, Observer { uiEvent ->
                uiEvent.getContentIfNotHandled()?.let {
                    if (it.isEmpty()) {
                        mbTotalPrice.makeGone()
                        ivEmptyCart.makeVisible()
                    }
                    cartList.addAll(it)
                    adapter.notifyDataSetChanged()
                }
            })

            updatedQuantity.observe(viewLifecycleOwner, Observer { uiEvent ->
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

            deletedCartItem.observe(viewLifecycleOwner, Observer {uiEvent->
                uiEvent.getContentIfNotHandled()?.let {
                    removeIndex?.let {
                        cartList.removeAt(it)
                        adapter.notifyItemRemoved(it)
                        viewModel.retrieveTotalCost()
                        if(cartList.isEmpty()){
                            ivEmptyCart.makeVisible()
                            mbTotalPrice.makeGone()
                        }
                    }
                }
            })
        }
    }

}