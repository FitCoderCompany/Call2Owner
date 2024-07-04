package com.call2owner.custom

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.call2owner.R

class TwoLinearText(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    constructor(context: Context) : this(context, null)

    private lateinit var text1: TextView
    private lateinit var text2: TextView
    lateinit var parent: LinearLayout

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        inflate(context, R.layout.layout_two_linear_text, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TwoLinearText)
        val t1 = typedArray.getString(R.styleable.TwoLinearText_title)
        val isBold = typedArray.getBoolean(R.styleable.TwoLinearText_isBold,false)
        val t2 = typedArray.getString(R.styleable.TwoLinearText_sub)
        val t3 = typedArray.getResourceId(R.styleable.TwoLinearText_subTextColor, R.color.black)
        typedArray.recycle()
        initComponents()
        title = t1 ?: ""
        sub = t2 ?: ""
        setBold(isBold)
        setVisi(sub.isEmpty())
        setSubColor(t3)
        invalidate()
        requestLayout()
    }

    private fun setBold(bold: Boolean?) {
        bold?.let {
            if (it) {
                text1.setTypeface(text1.typeface, Typeface.BOLD)
                text2.setTypeface(text2.typeface, Typeface.BOLD)
            }else{
                text1.setTypeface(text1.typeface, Typeface.NORMAL)
                text2.setTypeface(text2.typeface, Typeface.NORMAL)
            }
            invalidate()
            requestLayout()
        }
    }

    fun setTextSize(textSize: Float) {
        text1.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
        text2.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
        invalidate()
        requestLayout()
    }

    fun setSubColor(t3: Int) {
        text2.setTextColor(ContextCompat.getColor(context, t3))
        invalidate()
        requestLayout()
    }

    fun setVisi(empty: Boolean) {
        parent.visibility = if (empty) View.GONE else View.VISIBLE
        invalidate()
        requestLayout()
    }

    private fun initComponents() {
        text1 = findViewById(R.id.title)
        text2 = findViewById(R.id.sub)
        parent = findViewById(R.id.parentLayoutTwoText)
    }

    fun getSub(): TextView {
        return text2
    }

    var title: String
        get() {
            return if (text1.text == null) ""
            else text1.text.toString()
        }
        set(txt) {
            text1.text = txt
            invalidate()
            requestLayout()
        }

    var sub: String
        get() {
            return if (text2.text == null) ""
            else text2.text.toString()
        }
        set(txt) {
            text2.text = txt
            setVisi(txt.isEmpty())
            invalidate()
            requestLayout()
        }
}