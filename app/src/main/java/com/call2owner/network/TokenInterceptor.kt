package com.call2owner.network

import com.call2owner.network.Constant
import com.call2owner.utils.AppData
import com.call2owner.utils.MyUtil.log
import com.call2owner.utils.UserData
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor() : Interceptor {

    @Inject
    lateinit var userData: AppData
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder().addHeader("Authorization", "Bearer ${userData.authToken}").build()
        ("Modified Request: $newRequest").log()
        return chain.proceed(newRequest)
    }
}