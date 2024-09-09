package com.call2owner.custom

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.InputFilter
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.call2owner.R

class CustomTextView(context: Context, attrs: AttributeSet) : LinearLayout(context,attrs) {

    private var card: CardView?=null
    private var textLayout:TextInputLayout?=null
    private var editText: AppCompatEditText?=null
    private var error: TextView?=null
    private var actionBtn: TextView?=null
    private var icon :ImageView?=null

    init {
        init(attrs)
    }
    private fun init(attrs: AttributeSet) {
        inflate(context, R.layout.custom_edit_text, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView)
        val t = typedArray.getText(R.styleable.CustomTextView_texts)
        val auto = typedArray.getText(R.styleable.CustomTextView_autofillHints)
        val action = typedArray.getText(R.styleable.CustomTextView_action)
        val e = typedArray.getText(R.styleable.CustomTextView_error)
        val h = typedArray.getString(R.styleable.CustomTextView_hint)
        val l = typedArray.getInt(R.styleable.CustomTextView_maxLength,0)
        val d = typedArray.getDrawable(R.styleable.CustomTextView_startIconDrawable)
        val p = typedArray.getBoolean(R.styleable.CustomTextView_passwordToggleEnabled,false)
        val only = typedArray.getBoolean(R.styleable.CustomTextView_onlyClick,false)
        val en = typedArray.getBoolean(R.styleable.CustomTextView_enables,true)
        val drawableTintEnable = typedArray.getBoolean(R.styleable.CustomTextView_drawableTintEnable,true)
        val caps = typedArray.getBoolean(R.styleable.CustomTextView_textAllCaps,false)
        val di = typedArray.getString(R.styleable.CustomTextView_digits)
        val i = typedArray.getInt(R.styleable.CustomTextView_inputType, EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE)
        typedArray.recycle()
        initComponents()
        text= t?.toString() ?: ""
        setActions(action?.toString())
        setInBuildError(e)
        setHint(h)
        setFilter(l,caps)
        setStartPasswordToggleEnable(p)
        setOnlyClickEnable(only)
        setEnable(en)
        setDigits(di)
        setInputType(i)
        setHideError()
        setDrawableTintEnabled(drawableTintEnable)
        setStartDrawable(d)
        setAuto(auto)
        invalidate()
        requestLayout()

    }

    private fun setAuto(auto: CharSequence?) {
        if(auto!=null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                editText?.setAutofillHints(auto.toString())
            }
            invalidate()
            requestLayout()
        }
    }


    fun setActions(action: String?) {
        if(action.isNullOrEmpty()){
            actionBtn?.visibility=View.GONE
        }else{
            actionBtn?.text=action
            actionBtn?.visibility=View.VISIBLE
        }
        invalidate()
        requestLayout()
    }

    private fun setDrawableTintEnabled(drawableTintEnable: Boolean) {
        if(drawableTintEnable){
            icon?.imageTintList=ColorStateList.valueOf(ContextCompat.getColor(context,R.color.white))
        }
        else {
            icon?.imageTintList=null
        }
        invalidate()
        requestLayout()
    }

    private fun setHideError() {
        editText?.doAfterTextChanged {
            if(!it.isNullOrEmpty()){
                hideError()
                invalidate()
                requestLayout()
            }
        }
    }

    private fun setOnlyClickEnable(only: Boolean) {
        if(only){
            editText?.isClickable=true
            editText?.isFocusable=false
            editText?.isLongClickable=false
        }
    }

    fun setEnable(en: Boolean) {
        textLayout?.isEnabled=en
        invalidate()
        requestLayout()
    }

    private fun setDigits(di: String?) {
        if(di!=null) {
            editText?.keyListener = DigitsKeyListener.getInstance(di)

        }
    }

    private fun initComponents() {
        card=findViewById(R.id.card)
        textLayout = findViewById(R.id.layout)
        editText = findViewById(R.id.text)
        error=findViewById(R.id.error)
        actionBtn=findViewById(R.id.action)
        icon=findViewById(R.id.icon)
    }

    fun setError(msg:String){
        val color=ContextCompat.getColor(context,R.color.md_theme_error)
        val states = arrayOf(intArrayOf())
        val colors = intArrayOf(color)
        textLayout?.defaultHintTextColor=ColorStateList(states, colors)
        textLayout?.setStartIconTintList(ColorStateList(arrayOf(intArrayOf()), intArrayOf(ContextCompat.getColor(context,R.color.md_theme_error))))
        editText?.setTextColor(color)

        error?.text=msg
        error?.visibility= View.VISIBLE

        invalidate()
        requestLayout()
    }

    fun setError(msg:Int){
        val color=ContextCompat.getColor(context,R.color.md_theme_error)
        val states = arrayOf(intArrayOf())
        val colors = intArrayOf(color)
        textLayout?.defaultHintTextColor=ColorStateList(states, colors)
        textLayout?.setStartIconTintList(ColorStateList(arrayOf(intArrayOf()), intArrayOf(ContextCompat.getColor(context,R.color.md_theme_error))))
        editText?.setTextColor(color)

        error?.text=context.getString(msg)
        error?.visibility= View.VISIBLE

        invalidate()
        requestLayout()
    }

    fun setError(){
        val color=ContextCompat.getColor(context,R.color.md_theme_error)
        val states = arrayOf(intArrayOf())
        val colors = intArrayOf(color)
        textLayout?.defaultHintTextColor=ColorStateList(states, colors)
        textLayout?.setStartIconTintList(ColorStateList(arrayOf(intArrayOf()), intArrayOf(ContextCompat.getColor(context,R.color.md_theme_error))))
        editText?.setTextColor(color)

        error?.visibility= View.VISIBLE

        invalidate()
        requestLayout()
    }


    fun getEditView(): AppCompatEditText {
        return editText!!
    }
    fun getActionBtn(): TextView {
        return actionBtn!!
    }

    fun isEmpty():Boolean{
        return editText?.text.isNullOrEmpty()
    }

    fun getTextView():TextInputLayout{
        return textLayout!!
    }
    fun hideError(){
        val states = arrayOf(intArrayOf())

        val colors = intArrayOf(ContextCompat.getColor(context,R.color.black))
        textLayout?.defaultHintTextColor=ColorStateList(states, colors)
        textLayout?.setStartIconTintList(ColorStateList(arrayOf(intArrayOf()), intArrayOf(ContextCompat.getColor(context,R.color.colorPrimary))))

        val colors2 = intArrayOf(ContextCompat.getColor(context,R.color.colorPrimary))
        textLayout?.hintTextColor=ColorStateList(states, colors2)

        editText?.setTextColor(ContextCompat.getColor(context, R.color.black))
        error?.visibility= View.GONE

        invalidate()
        requestLayout()
    }
    private fun setStartPasswordToggleEnable(p: Boolean) {
        if(p){
            textLayout?.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        }else{
            textLayout?.endIconMode = TextInputLayout.END_ICON_NONE
        }
    }

    fun clearText(){
        editText?.setText("")
        editText?.clearFocus()
    }


    var text: String
        get() =
            if (editText?.text.isNullOrEmpty())
                ""
            else
                editText?.text?.toString()?.trim() ?: ""


        set(txt) {
            editText?.setText(txt)
            invalidate()
            requestLayout()
        }

    private fun setInBuildError(e: CharSequence?) {
        error?.text = e
    }

    fun setHint(h: String?) {
        textLayout?.hint = h
        invalidate()
        requestLayout()
    }

    private fun setFilter(l: Int, caps: Boolean) {
        if(l!=0 && caps){
            editText?.filters = arrayOf(InputFilter.LengthFilter(l), InputFilter.AllCaps())
        }
        else if(l==0 &&caps){
            editText?.filters = arrayOf(InputFilter.AllCaps())
        }else if(l!=0 && !caps){
            editText?.filters = arrayOf(InputFilter.LengthFilter(l))
        }

    }

    fun setStartDrawable(d: Drawable?) {
        icon?.setImageDrawable(d)
        invalidate()
        requestLayout()
    }

    fun setStartDrawable(d: Bitmap?) {
        icon?.setImageBitmap(d)
        icon?.imageTintList=null

        invalidate()
        requestLayout()
    }

    private fun setInputType(i: Int?) {
        if(i==null)
            editText?.inputType=EditorInfo.TYPE_CLASS_TEXT
        else
            editText?.inputType=i
    }
}
