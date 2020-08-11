package com.clevmania.leosbook.ui.books.detail

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.clevmania.leosbook.ui.books.detail.description.DescriptionFragment
import com.clevmania.leosbook.ui.books.detail.product.ProductDetailFragment
import com.clevmania.leosbook.ui.books.detail.reviews.ReviewsFragment

/**
 * @author by Lawrence on 8/3/20.
 * for LeosBook
 */
const val DESCRIPTION_PAGE_INDEX = 0
const val PRODUCT_PAGE_INDEX = 1
const val REVIEWS_PAGE_INDEX = 2

class BookDetailsAdapter(fragment: Fragment, viewModel: BookDetailViewModel) : FragmentStateAdapter(fragment) {
    private val tabCreators: Map<Int, () -> Fragment> = mapOf(
        DESCRIPTION_PAGE_INDEX to { DescriptionFragment.newInstance(viewModel) },
        PRODUCT_PAGE_INDEX to { ProductDetailFragment() },
        REVIEWS_PAGE_INDEX to { ReviewsFragment() }
    )

    override fun getItemCount() = tabCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}