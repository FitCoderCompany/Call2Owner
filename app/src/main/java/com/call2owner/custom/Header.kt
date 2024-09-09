package com.call2owner.custom

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.call2owner.R

class Header(context: Context, attrs: AttributeSet) : ConstraintLayout(context,attrs) {
    private var title: TextView?=null
    private var back :ImageView?=null
    private var action :ImageView?=null

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet) {
        inflate(context, R  .layout.layout_header, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Header)
        val t = typedArray.getText(R.styleable.Header_heading)
        val action = typedArray.getResourceId(R.styleable.Header_actionSrc,R.drawable.filter)
        val b = typedArray.getBoolean(R.styleable.Header_back,true)

        typedArray.recycle()
        initComponents()
        setBackAction(b)
        setActions(action)
        heading=t?.toString()?:""
        invalidate()
        requestLayout()

    }

    private fun setActions(icon: Int) {
        if(icon==R.drawable.filter){
            action?.visibility=View.GONE
        }else{
            action?.visibility=View.VISIBLE
            action?.setImageResource(icon)
        }
        invalidate()
        requestLayout()
    }

    fun getAction(): ImageView? {
        return action
    }

    private fun initComponents() {
        title=findViewById(R.id.title)
        back=findViewById(R.id.backBtn)
        action=findViewById(R.id.action)
    }


    var heading :String
        get() {
          return  title?.text?.toString()?:""
        }
        set(value) {
            title?.text=value
            invalidate()
            requestLayout()
        }


    private fun setBackAction(b: Boolean) {
        back?.setOnClickListener {
            (context as? Activity)?.onBackPressed()
        }
        back?.visibility=if(b) View.VISIBLE else View.GONE

        invalidate()
        requestLayout()
    }
}
