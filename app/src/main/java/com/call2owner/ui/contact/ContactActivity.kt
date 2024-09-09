package com.call2owner.ui.contact

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import com.call2owner.R
import com.call2owner.databinding.ActivityContactBinding
import com.call2owner.ui.BaseActivity

class ContactActivity : BaseActivity() {
    private lateinit var binding: ActivityContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.apply {
            mobile.text="9177775555"
            email.text="help@call2owner.com"
            address.text="Unit No. 388, 3rd Floor, Vegas Mall,  Sector 14 Dwarka,  Delhi, 110075"

            loadWeb(webView)
            mobile.getActionBtn().setOnClickListener{
                callNumber(mobile.text)
            }
            email.getActionBtn().setOnClickListener{
                email(email.text)
            }
            address.getActionBtn().setOnClickListener{
                val lat = "28.601251"
                val lang = "77.030258"
                openMap(lat,lang)
            }
        }
    }

    private fun openMap(lat: String, lang: String) {
        val title = getString(R.string.company_name)
        val geoUri = "http://maps.google.com/maps?q=loc:$lat,$lang ($title)"
        Intent(Intent.ACTION_VIEW).also {
            it.data = Uri.parse(geoUri)
            startActivity(it)
        }
    }

    private fun email(text: String) {
        try {
            var body: String? = null
            try {
                body =  packageManager.getPackageInfo( packageName, 0).versionName
                body =
                    """
                            -----------------------------
                            Please don't remove this information
                            Device OS: Android 
                            Device OS version: ${Build.VERSION.RELEASE}
                            App Version: $body
                            Device Brand: ${Build.BRAND}
                            Device Model: ${Build.MODEL}
                            Device Manufacturer: ${Build.MANUFACTURER}"""
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:$text")
            intent.putExtra(
                Intent.EXTRA_SUBJECT,
                "Query from " +  getString(R.string.app_name) + " android app"
            )
            intent.putExtra(Intent.EXTRA_TEXT, body)
            startActivity(
                Intent.createChooser(
                    intent,
                    getString(R.string.chooser)
                )
            )

        } catch (e: Exception) {
            Toast.makeText(context, "Unable to open", Toast.LENGTH_SHORT).show()
        }
    }

    private fun callNumber(text: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$text")
        startActivity(intent)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWeb(webView: WebView) {
        val frm="<iframe src=\"https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3502.950855396112!2d77.0302578!3d28.601251100000002!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x390d1b0641061b8d%3A0x5efd8be14cbc0e17!2sYoke%20Payment%20India%20Pvt%20Ltd!5e0!3m2!1sen!2sin!4v1718694449493!5m2!1sen!2sin\" width=\"100%\" height=\"100%\" style=\"border:0;\" allowfullscreen=\"\" loading=\"lazy\" referrerpolicy=\"no-referrer-when-downgrade\"></iframe>"
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.loadsImagesAutomatically = true
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW // Prevent mixed content issues
        webView.setWebChromeClient(object : WebChromeClient() {
            override fun onGeolocationPermissionsShowPrompt(origin: String, callback: GeolocationPermissions.Callback) {
                callback.invoke(origin, true, true)
            }
        })

        webView.loadDataWithBaseURL(null, frm, "text/html", "UTF-8", null)

    }

}