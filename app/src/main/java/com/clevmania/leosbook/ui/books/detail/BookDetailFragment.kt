package com.clevmania.leosbook.ui.books.detail

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.clevmania.leosbook.R
import com.clevmania.leosbook.constants.Constants
import com.clevmania.leosbook.data.Cart
import com.clevmania.leosbook.data.FirebaseUtils
import com.clevmania.leosbook.extension.formatPrice
import com.clevmania.leosbook.ui.TopLevelFragment
import com.clevmania.leosbook.ui.books.detail.model.BookDetailResponse
import com.clevmania.leosbook.utils.BadgeUtils.convertLayoutToImage
import com.clevmania.leosbook.utils.GlideApp
import com.clevmania.leosbook.utils.InjectorUtils
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.book_detail_fragment.*

class BookDetailFragment : TopLevelFragment() {
    private val args : BookDetailFragmentArgs by navArgs()
    private lateinit var bookInfo : BookDetailResponse

    private lateinit var viewModel: BookDetailViewModel
    private lateinit var viewModelFactory: BookDetailViewModelFactory

    companion object {
        fun newInstance() = BookDetailFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelFactory = InjectorUtils.provideBookDetailViewModelFactory(requireContext())
        viewModel = ViewModelProvider(
            this, viewModelFactory).get(BookDetailViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        viewModel.increaseCartCounter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.book_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.retrieveBookDetail(args.id)
        vpBookDetailsPager.adapter = BookDetailsAdapter(this,viewModel)
        TabLayoutMediator(tlBookDetails,vpBookDetailsPager){ tab, position ->
            tab.text = getTabTitle(position)
        }.attach()
        mbAddToCart.setOnClickListener { addToCart() }
    }

    private fun getTabTitle(pos : Int): String?{
        return when(pos){
            DESCRIPTION_PAGE_INDEX -> getString(R.string.description_page_title)
            PRODUCT_PAGE_INDEX -> getString(R.string.product_page_title)
            REVIEWS_PAGE_INDEX -> getString(R.string.reviews_page_title)
            else -> null
        }
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

            bookDetail.observe(viewLifecycleOwner, Observer { uiEvent ->
                uiEvent.getContentIfNotHandled()?.let {
                    populateView(it)
                }
            })

            itemInCart.observe(viewLifecycleOwner, Observer { uiEvent ->
                uiEvent.getContentIfNotHandled()?.let {
                    showSuccessDialog("Added To Cart", "Success")
                    increaseCartCounter()
                }
            })
        }
    }

    private fun populateView(it: BookDetailResponse) {
        bookInfo = it
        tvBookTitle.text = it.volumeInfo.title
        tvBookPrice.text = getString(R.string.price,it.volumeInfo.pageCount.formatPrice())
        tvBookAuthor.text = it.volumeInfo.authors.toString()
        rbBooksRating.rating = it.volumeInfo.ratingsCount.toFloat()
        GlideApp.with(requireView())
            .load(it.volumeInfo.imageLinks.thumbnail)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .transform(CenterCrop())
            .into(ivBookPreview)
        viewModel.saveDescription(it.volumeInfo.description ?: getString(R.string.none_available))
    }

    private fun addToCart(){
        val cartItem = Cart(bookInfo.id,bookInfo.volumeInfo.title,
            bookInfo.volumeInfo.imageLinks.thumbnail
            ,bookInfo.volumeInfo.pageCount,
            Constants.BOOK_QUANTITY, FirebaseUtils.getUID()!!)

        viewModel.addBookToCart(cartItem)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.action_cart -> {
                findNavController().navigate(R.id.action_bookDetailFragment_to_cartFragment)
                true
            }
            R.id.action_fav -> {

                true
            }
            else -> false
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_book_detail, menu)
        val cartMenu = menu.findItem(R.id.action_cart)
        viewModel.cartCounter.observe(viewLifecycleOwner, Observer {uiEvent ->
            uiEvent.getContentIfNotHandled()?.let {
                cartMenu.icon = convertLayoutToImage(
                    requireContext(),it,R.drawable.ic_shopping_cart)
            }
        })
    }

}