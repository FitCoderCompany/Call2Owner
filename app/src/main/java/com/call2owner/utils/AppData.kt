package com.call2owner.utils

import android.content.Context
import com.call2owner.network.Constant
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppData @Inject constructor(@ApplicationContext private val context: Context) {

    private val sp = context.getSharedPreferences(Constant.sharePrefName, Context.MODE_PRIVATE)
    private val editor = sp.edit()




    var authToken: String
        get() = sp.getString("authToken", "") ?: ""
        set(v) = editor.putString("authToken", v).apply()

    var latitude: String
        get() = sp.getString("lat", "") ?: ""
        set(v) = editor.putString("lat", v).apply()

    var longitude: String
        get() = sp.getString("lng", "") ?: ""
        set(v) = editor.putString("lng", v).apply()

    var addressByLocation: String
        get() = sp.getString("addressByLocation", "") ?: ""
        set(v) = editor.putString("addressByLocation", v).apply()

    var bannerImages: String
        get() = sp.getString("bannerImages", "") ?: ""
        set(v) = editor.putString("bannerImages", v).apply()

    var productDetails: String
        get() = sp.getString("productDetails", "") ?: ""
        set(v) = editor.putString("productDetails", v).apply()
}