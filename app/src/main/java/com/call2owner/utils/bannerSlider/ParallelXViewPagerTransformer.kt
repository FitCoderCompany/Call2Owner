package com.call2owner.utils.bannerSlider
import android.view.View
import androidx.viewpager.widget.ViewPager

private const val MIN_SCALE = 0.85f
private const val MIN_ALPHA = 0.5f

class ParallelXViewPagerTransformer : ViewPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val width = page.width
        val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
        if (position < -1) {
            page.scrollX = (width * 0.75 * -1).toInt()
            page.alpha=1f
        } else if (position <= 1) {

            if (position < 0) {
                page.scrollX = (width * 0.75 * position).toInt()
                page.alpha=(MIN_ALPHA +(((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
            } else {
                page.scrollX = (width * 0.75 * position).toInt()
            }
        } else {
            page.scrollX = (width * 0.75).toInt()
            page.alpha=1f
        }
    }
}
//
//class ParallelXViewPagerTransformer(private val context: Context) : ViewPager.PageTransformer {
//    override fun transformPage(page: View, position: Float) {
//        val width = page.width
//        val shadowFactor = 0.9f
//
//        if (position < -1) {
//            page.scrollX = (width * shadowFactor * -1).toInt()
//        } else if (position <= 1) {
//            if (position < 0) {
//                page.scrollX = (width * shadowFactor * position).toInt()
//            } else {
//                page.scrollX = (width * shadowFactor * position).toInt()
//            }
//
//            // Ensure page has a background drawable
//            page.background?.let { drawable ->
//                // Adjust the background alpha based on the position
//                val alphaFactor = Math.max(0.2f, 1 - Math.abs(position))
//                drawable.alpha = (alphaFactor * 255).toInt()
//            }
//        } else {
//            page.scrollX = (width * shadowFactor).toInt()
//        }
//    }
//
//    init {
//        // Set the shadow drawable as the background for each page
//        (context as Activity).runOnUiThread {
//            context.findViewById<ViewPager>(R.id.viewPager)?.adapter?.apply {
//                for (i in 0 until count) {
//                    val page = instantiateItem(context.findViewById<ViewPager>(R.id.viewPager), i) as View
//                    page.setBackgroundResource(R.drawable.shadow)
//                }
//            }
//        }
//    }
//}
