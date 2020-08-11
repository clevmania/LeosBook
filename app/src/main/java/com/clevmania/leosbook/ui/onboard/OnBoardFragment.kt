package com.clevmania.leosbook.ui.onboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.clevmania.leosbook.R
import com.clevmania.leosbook.extension.makeGone
import com.clevmania.leosbook.extension.makeVisible
import com.clevmania.leosbook.ui.AuthFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_on_board.*

class OnBoardFragment : AuthFragment() {
    private lateinit var sliderAdapter : OnBoardAdapter

    override fun onStart() {
        super.onStart()
        vpSlidePager.registerOnPageChangeCallback(pageChangeListener)
        TabLayoutMediator(tvTabLayout,vpSlidePager) { _, _ ->  }.attach()
    }

    private val pageChangeListener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            when (position) {
                3 -> {
                    btnNext.text = getString(R.string.start_screen)
                    btnSkip.makeGone()
                }
                else -> {
                    btnNext.text = getString(R.string.next_slide)
                    btnSkip.makeVisible()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadSlider()
        showNextSlide()
    }

    private fun showNextSlide(){
        btnNext.setOnClickListener {
            if(vpSlidePager.currentItem + 1 < sliderAdapter.itemCount){
                vpSlidePager.currentItem += 1
            }else{
                // Navigate to Login Fragment
                if(findNavController().currentDestination?.id == R.id.onBoardFragment){
                    findNavController().navigate(R.id.action_onBoardFragment_to_signInFragment)
                }
            }
        }
        btnSkip.setOnClickListener{
            if(findNavController().currentDestination?.id == R.id.onBoardFragment){
                findNavController().navigate(R.id.action_onBoardFragment_to_signInFragment)
            }
        }
    }

    private fun loadSlider(){
        val sliderList =  listOf(
            SliderList(getString(R.string.find_books),
                getString(R.string.find_book_body), R.drawable.img_find_books),
            SliderList(getString(R.string.seamless_payment),
                getString(R.string.seamless_payment_body), R.drawable.img_make_payment),
            SliderList(getString(R.string.view_transactions),
                getString(R.string.view_transactions_body), R.drawable.img_view_transactions),
            SliderList(getString(R.string.refunds),
                getString(R.string.refund_customers_body), R.drawable.img_refund)
        )

        sliderAdapter = OnBoardAdapter(sliderList)
        vpSlidePager.adapter = sliderAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
//        vpSlidePager.unregisterOnPageChangeCallback(pageChangeListener)
    }
}