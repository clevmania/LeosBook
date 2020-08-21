package com.clevmania.leosbook.ui.books.detail

import android.content.ComponentName
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.clevmania.leosbook.R
import com.clevmania.leosbook.constants.Constants
import com.clevmania.leosbook.data.local.Cart
import com.clevmania.leosbook.data.FirebaseUtils
import com.clevmania.leosbook.extension.formatPrice
import com.clevmania.leosbook.extension.makeVisible
import com.clevmania.leosbook.ui.base.TopLevelFragment
import com.clevmania.leosbook.ui.books.detail.model.BookDetailResponse
import com.clevmania.leosbook.utils.BadgeUtils.convertLayoutToImage
import com.clevmania.leosbook.utils.GlideApp
import com.clevmania.leosbook.utils.InjectorUtils
import com.clevmania.leosbook.utils.UiUtils
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.book_detail_fragment.*

class BookDetailFragment : TopLevelFragment() {
    private val args : BookDetailFragmentArgs by navArgs()
    private lateinit var bookInfo : BookDetailResponse

    private lateinit var viewModel: BookDetailViewModel
    private lateinit var viewModelFactory: BookDetailViewModelFactory

    private lateinit var customTabsIntent : CustomTabsIntent
    private var customTabsClient : CustomTabsClient? = null
    private var customTabsSession: CustomTabsSession? = null

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
        mbPreviewBook.setOnClickListener { launchMediaHandle(bookInfo.accessInfo.webReaderLink) }
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
        tvBookAuthor.text = UiUtils.getAuthorsOrCategories(it.volumeInfo.authors)
        rbBooksRating.rating = it.volumeInfo.ratingsCount.toFloat()
        GlideApp.with(requireView())
            .load(it.volumeInfo.imageLinks.thumbnail)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .transform(CenterCrop())
            .into(ivBookPreview)
        viewModel.saveDescription(it.volumeInfo.description ?: getString(R.string.none_available))
        grpBookDetail.makeVisible()
    }

    private fun addToCart(){
        val cartItem = Cart(
            bookInfo.id, bookInfo.volumeInfo.title,
            bookInfo.volumeInfo.imageLinks.thumbnail
            , bookInfo.volumeInfo.pageCount,
            Constants.BOOK_QUANTITY, FirebaseUtils.getUID()!!
        )

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

    private fun launchMediaHandle(webUrl : String){
        val connection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(name: ComponentName, client: CustomTabsClient) {
                customTabsClient = client
                customTabsClient?.warmup(0L)
                customTabsSession = customTabsClient!!.newSession(null)
            }

            override fun onServiceDisconnected(name: ComponentName) {
                customTabsClient = null
            }
        }
        CustomTabsClient.bindCustomTabsService(requireContext(),Constants.CHROME_PACKAGE,connection)
        customTabsIntent = CustomTabsIntent.Builder(customTabsSession)
            .setShowTitle(true)
            .setStartAnimations(requireContext(), R.anim.slide_in_right, R.anim.slide_out_left)
            .setExitAnimations(requireContext(), R.anim.slide_in_left, R.anim.slide_out_right)
            .setToolbarColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            .build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(webUrl))
    }

}