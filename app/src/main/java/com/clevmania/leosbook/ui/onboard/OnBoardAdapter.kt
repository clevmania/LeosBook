package com.clevmania.leosbook.ui.onboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.clevmania.leosbook.R
import kotlinx.android.synthetic.main.item_slider.view.*

/**
 * @author by Lawrence on 7/23/20.
 * for FluExpert
 */
class OnBoardAdapter(private val sliderList : List<SliderList>): RecyclerView.Adapter<OnBoardAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_slider,parent,false))
    }

    override fun getItemCount() = sliderList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(sliderList[position])
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindView(sliderList: SliderList) {
            itemView.ivSlider.setImageResource(sliderList.backgroundImg)
            itemView.tvSlideHeader.text = sliderList.title
            itemView.tvSliderBody.text = sliderList.body
        }
    }
}

data class SliderList(var title : String, var body: String, var backgroundImg : Int)