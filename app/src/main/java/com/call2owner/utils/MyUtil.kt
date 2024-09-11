package com.call2owner.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target
import com.call2owner.BuildConfig
import com.call2owner.R
import com.call2owner.databinding.LayoutDialogBinding
import com.call2owner.databinding.LayoutNoInternetBinding
import com.call2owner.network.Constant
import com.call2owner.ui.activity.WebActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.text.DecimalFormat
import java.util.Locale
import java.util.regex.Pattern

object MyUtil {

    fun String.log(){
        if(BuildConfig.DEBUG)
            Log.d(Constant.TAG,this)
    }

    fun Exception.log(){
        if(BuildConfig.DEBUG) {
            Log.e(Constant.TAG, this.toString())
            this.printStackTrace()
        }
    }
    fun Context.showSnackBar(window: Window, msg: String?) {
//        val customView = LayoutInflater.from(this).inflate(R.layout.layout_snackbar,null)
//
//        val snackBar = Snackbar.make(window.decorView, msg ?: getString(R.string.something_wrong), Snackbar.LENGTH_LONG)
//
//        val snackbarLayout = snackBar.view as Snackbar.SnackbarLayout
//
//        snackbarLayout.addView(customView, 0)
//        snackBar.show()




        val snackbar = Snackbar.make(window.decorView, msg?:"Something went wrong", Snackbar.LENGTH_LONG)
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.colorPrimary))
        snackbar.setTextColor(Color.BLACK)
        snackbar.show()

//        val v = snackBar.view
//        val params = v.layoutParams as FrameLayout.LayoutParams
//
//        params.gravity = Gravity.TOP
//        params.setMargins(0, 0, 0, 100)
//        v.layoutParams = params
//
//        val textView = v.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
//        textView.setTextColor(Color.BLACK)
//
//        v.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
//        snackBar.show()

    }



    fun Context.showSnackBar(window: Window, msg: String, btnText: String, btnFun: () -> Unit) {
        val snackBar = Snackbar.make(window.decorView, msg, Snackbar.LENGTH_LONG)
        snackBar.setAction(btnText) { btnFun() }
        val v = snackBar.view
        val params = v.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        params.setMargins(0, 100, 0, 0)
        v.layoutParams = params
        v.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        snackBar.show()
    }

    fun Context.showNoInternetDialog( activity: Activity  ): Pair<Button, AlertDialog> {
        val alertDialog = AlertDialog.Builder(this, R.style.AlertDialogStyle)
        val b = LayoutNoInternetBinding.inflate(activity.layoutInflater)
        alertDialog.setView(b.root)
        alertDialog.setCancelable(false)
        val alert = alertDialog.create()
        alert.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return Pair(b.closeApp,alert)
    }


    fun Activity.hideKeyboard() {
        try {
            val view: View? = currentFocus
            if (view != null) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        } catch (e: java.lang.Exception) {
            e.log()
        }
    }

    fun Context.toast(msg:String){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }

    fun Context.showSimpleAlertDialog (title: String, msg: String, cancelable: Boolean, func: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(msg)
            .setCancelable(cancelable)
            .setPositiveButton(R.string.ok) { d, _ ->
                d.dismiss()
                func()
            }.show()
    }

    fun <T> Activity.start(context: Context, cl: Class<T>) {
        startActivity(Intent(context, cl))
    }

    fun Context.explicitWeb(web:String){
        val browserIntent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(web))
        startActivity(browserIntent)
    }

    fun Context.openImplicitWeb(web: String) {
        Intent(this, WebActivity::class.java).run{
            putExtra("url",web)
            startActivity(this)
        }
    }

    fun Context.copyToClipboard(text: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("${getString(R.string.app_name)} : Copied Text", text)
        clipboardManager.setPrimaryClip(clip)
    }


    fun Context.vibratePhone() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(80, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(80)
        }
    }



    fun ImageView.setImage(url:String?, placeholder:Int?=R.drawable.app_logo_svg){
        placeholder?.let {
            Glide.with(this).load(url?.trim()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(
                it
            ).into(this)
        }
    }

    fun String.isValidEmail(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }


    fun Context.getProviderName(): String {
        return  getString(R.string.provider_name)
    }
    fun String.getLastStringAfterDot( ): String {
        val pattern = Pattern.compile("[^.]+$")
        val matcher = pattern.matcher(this)
        return if (matcher.find()) {
            matcher.group(0)!!
        } else {
            this
        }
    }

    fun <T> String.model(c: Class<T>): T? {
        return try {
            Gson().fromJson(this, c)
        }catch (e:Exception){
            e.log()
            null
        }
    }

    fun String?.capWord():String {
        return (this?:"").split(" ").joinToString(" ") {
            it.replaceFirstChar { i->
                i.titlecase()
            }
        }
    }

    fun capitalizeWords(input: String): String {
        return input.split(" ").joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { it.uppercase() }
        }
    }

    fun String?.doubleCurrency(): String {
        var value = "0.00"
        try {
            val formatter = DecimalFormat("#,##,##0.00")
//                val formatter: DecimalFormat = NumberFormat.getNumberInstance(Locale.getDefault()) as DecimalFormat
            value = if (this != null) {
                try {
                    formatter.format(this.trim().toDouble())
                } catch (e: Exception) {
                    e.log()
                    ""
                }
            } else {
                formatter.format(0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "₹ $value"
    }

    fun fitDialog(
        context: Context,
        activity: Activity,
        title: String = "",
        msg: String = "",
        okTxt: String = "",
        cancelTxt: String = "",
        cancelable: Boolean = true,
        icon: Int? = null,
        toShow: Boolean = false,
        okFun: () -> Unit
    ): Pair<AlertDialog, LayoutDialogBinding> {
        val alertDialog = AlertDialog.Builder(context, R.style.AlertDialogStyle)
        val b = LayoutDialogBinding.inflate(activity.layoutInflater)

        if (icon == null) {
            b.icon.visibility = View.GONE
        } else {
            b.icon.visibility = View.VISIBLE
            b.icon.setAnimation(icon)
        }

        if (title.isEmpty()) {
            b.title.visibility = View.GONE
        } else {
            b.title.visibility = View.VISIBLE
            b.title.text = title
        }

        if (msg.isEmpty()) {
            b.message.visibility = View.GONE
        } else {
            b.message.visibility = View.VISIBLE
            b.message.text = msg
        }

        b.ok.text = okTxt.ifEmpty { context.getString(R.string.ok) }

        if (cancelTxt.isEmpty()) {
            b.cancel.visibility = View.GONE
        } else {
            b.cancel.visibility = View.VISIBLE
            b.cancel.text = cancelTxt
        }

        alertDialog.setView(b.root)
        alertDialog.setCancelable(cancelable)
        val alert = alertDialog.create()
        alert.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        b.cancel.setOnClickListener { alert.dismiss() }

        b.ok.setOnClickListener {
            okFun()
            alert.dismiss()
        }
        if (toShow) {
            alert.show()
        }
        return Pair(alert, b)
    }

    fun getCommaCurrency(text: String?): String {
        var value = "0.00"
        try {
            val formatter = DecimalFormat("#,##,##0.00")
//                val formatter: DecimalFormat = NumberFormat.getNumberInstance(Locale.getDefault()) as DecimalFormat
            value = if (text != null) {
                try {
                    formatter.format(text.trim().toDouble())
                } catch (e: Exception) {
                    e.log()
                    ""
                }
            } else {
                formatter.format(0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "₹ $value"
    }

    fun getCommaCurrency(text: Double?): String {
        var value = "0.00"
        try {
            val formatter = DecimalFormat("#,##,##0.00")
//                val formatter: DecimalFormat = NumberFormat.getNumberInstance(Locale.getDefault()) as DecimalFormat
            value = if (text != null) {
                try {
                    formatter.format(text )
                } catch (e: Exception) {
                    e.log()
                    ""
                }
            } else {
                formatter.format(0)
            }
        } catch (e: Exception) {
            e.log()
        }
        return "₹ $value"
    }

    fun TextView.changeText(newText:String, duration: Long = 200) {
        animate().scaleY(0f).setDuration(duration / 2).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                text = newText
                animate().scaleY(1f).setDuration(duration / 2).setListener(null)
            }
        })
    }

}