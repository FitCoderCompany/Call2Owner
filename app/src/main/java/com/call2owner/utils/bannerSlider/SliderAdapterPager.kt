package com.call2owner.utils.bannerSlider

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.call2owner.databinding.ItemSliderImageBinding

class SliderAdapterPager(
    var context: Context,
    private var arrayList: List<String?>,
    var layoutInflater: LayoutInflater,
    val handler: Handler
) : PagerAdapter() {

    private val autoScrollInterval = 3000L // interval in seconds
    private var viewPager: ViewPager? = null


    override fun getCount(): Int {
        return arrayList.size
    }

    override fun isViewFromObject(view: View, viewObject: Any): Boolean {
        return view == viewObject;
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    @SuppressLint("InflateParams")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val newView  = ItemSliderImageBinding.inflate(layoutInflater)
        Glide.with(context)
            .load(arrayList[position])
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(newView.image)
        container.addView(newView.root)
        return newView.root
    }

    fun startAutoScroll(viewPager: ViewPager) {
        this.viewPager = viewPager
        handler.postDelayed(autoScrollRunnable, autoScrollInterval)
    }

    fun stopAutoScroll() {
        handler.removeCallbacks(autoScrollRunnable)
    }

    private val autoScrollRunnable = object : Runnable {
        override fun run() {
            viewPager?.let {
                val currentItem = it.currentItem
                val nextItem = (currentItem + 1) % count
                it.setCurrentItem(nextItem, true)
                handler.postDelayed(this, autoScrollInterval)
            }
        }
    }
}