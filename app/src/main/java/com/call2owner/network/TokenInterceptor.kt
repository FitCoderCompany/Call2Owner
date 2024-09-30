package com.call2owner.network

import com.call2owner.utils.AppData
import com.call2owner.utils.MyUtil.log
import com.call2owner.utils.UserData
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor() : Interceptor {

    @Inject
    lateinit var appData: AppData
    @Inject
    lateinit var userData: UserData
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer ${appData.authToken}")
            .addHeader("deviceKey",userData.deviceKey)
            .addHeader("UID",userData.id)
            .build()
        ("Modified Request: $newRequest").log()
        return chain.proceed(newRequest)
    }
}