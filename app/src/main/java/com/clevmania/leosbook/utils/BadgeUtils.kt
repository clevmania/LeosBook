package com.clevmania.leosbook.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import com.clevmania.leosbook.R
import kotlinx.android.synthetic.main.item_badge.view.*

/**
 * @author by Lawrence on 8/3/20.
 * for LeosBook
 */
object BadgeUtils {
    fun convertLayoutToImage(context: Context, count: Int, drawableId: Int): Drawable? {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_badge, null)
        view.ivBadge.setImageResource(drawableId)

        if (count == 0) {
            view.clBadgeContainer.visibility = View.GONE
        } else {
            view.tvBadgeCount.text = count.toString()
        }
        view.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        val bitmap = Bitmap.createBitmap(view.measuredWidth,view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return BitmapDrawable(context.resources, bitmap)
    }
}