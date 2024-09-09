package com.call2owner.ui.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.call2owner.R
import com.call2owner.databinding.ActivityWebBinding
import com.call2owner.databinding.LayoutLoadingBinding
import com.call2owner.ui.BaseActivity
import com.call2owner.utils.MyUtil.log
import com.google.android.material.internal.ViewUtils.dpToPx

class WebActivity : BaseActivity() {
    private lateinit var binding: ActivityWebBinding
    private var url=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoader("Cancel")
        initView()
    }

    private fun initView() {
        url=intent.getStringExtra("url")?:""

        if(url.isEmpty()){
            showErrorSnackBar("Invalid URL")
        }else{
            startWebPage()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun startWebPage() {
        binding.apply {
            webView.webViewClient = WebViewClient()
            webView.settings.javaScriptEnabled = true
            webView.loadUrl(url)
            webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH)
            webView.getSettings().domStorageEnabled=true

            webView.webViewClient=MyWebViewClient()

//            webView.setWebChromeClient(object : WebChromeClient() {
//                override fun onGeolocationPermissionsShowPrompt(origin: String, callback: GeolocationPermissions.Callback) {
//                    callback.invoke(origin, true, true)
//                }
//            })
////
//            webView.getSettings().allowFileAccess = false
//            webView.getSettings().allowContentAccess = false
        }
    }


    inner class MyWebViewClient : WebViewClient() {
        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            return false
        }
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            hideLoader()
        }
        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
            hideLoader()
        }
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }


    private var loaderAlert: AlertDialog? = null

    private fun initLoader(loaderMessage: String) {
        val alertDialog = AlertDialog.Builder(context)
        val lb = LayoutLoadingBinding.inflate(layoutInflater)
        if (loaderMessage.isEmpty()) {
            lb.msg.visibility = View.GONE
        } else {
            lb.msg.run {
                visibility = View.VISIBLE
                text = loaderMessage
                setOnClickListener{finish()}
            }
        }
        alertDialog.setView(lb.root)
        alertDialog.setCancelable(false)
        loaderAlert = alertDialog.create()
        loaderAlert?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun showLoader(loaderMessage: String) {
        initLoader(loaderMessage)
        try {
            if (loaderAlert?.isShowing != true)
                loaderAlert?.show()
        } catch (e: Exception) {
            showLoader(loaderMessage)
        }
    }

    fun hideLoader() {
        try {
            if (loaderAlert?.isShowing == true) {
                loaderAlert?.dismiss()
            }
        } catch (e: Exception) {
            e.log()
        } finally {
            loaderAlert = null
        }
    }

}