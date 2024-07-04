package com.call2owner.network

import okhttp3.ResponseBody
import retrofit2.Call

interface NetworkInterface  {
    fun onResult(type: String,success:Boolean, response: String)
    fun onNoInternet(  )
}