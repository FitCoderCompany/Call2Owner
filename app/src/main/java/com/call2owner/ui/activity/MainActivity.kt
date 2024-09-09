package com.call2owner.ui.activity

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.call2owner.R
import com.call2owner.databinding.ActivityMainBinding
import com.call2owner.model.TokenResponse
import com.call2owner.ui.BaseActivity
import com.call2owner.utils.MyUtil.model

class MainActivity : BaseActivity() {

    var navController: NavController?=null
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.navHost)
        binding.navView.setupWithNavController(navController!!)
    }



//    override fun onResult(type: String, success: Boolean, response: String) {
//        when(type){  }
//    }

}