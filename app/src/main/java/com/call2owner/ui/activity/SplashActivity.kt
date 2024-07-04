package com.call2owner.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.call2owner.R
import com.call2owner.databinding.ActivitySplashBinding
import com.call2owner.ui.BaseActivity
import com.call2owner.utils.MyUtil.hideSystemBars
import com.call2owner.utils.animation.Animation.fadeIn

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    private lateinit var binding:ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        enableEdgeToEdge()

    }

    private fun initView() {
        binding.imageView.fadeIn()

        Handler(Looper.getMainLooper()).postDelayed({
            start(MainActivity::class.java)
            finish()
        },1000)

    }
}