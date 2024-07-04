package com.call2owner.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
class NonScrollableRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

//    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        // Disable touch events so it doesn't scroll
//        return ev.action == MotionEvent.ACTION_MOVE || super.dispatchTouchEvent(ev)
//    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        // Measure height fully and wrap content
        val heightSpecCustom = MeasureSpec.makeMeasureSpec(
            Integer.MAX_VALUE shr 2, MeasureSpec.AT_MOST
        )
        super.onMeasure(widthSpec, heightSpecCustom)
        layoutParams.height = measuredHeight
    }
}