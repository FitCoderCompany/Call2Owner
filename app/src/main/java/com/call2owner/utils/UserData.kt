package com.call2owner.utils

import android.content.Context
import com.call2owner.model.VerifyOtpResponse
import com.call2owner.network.Constant
import com.call2owner.utils.MyUtil.log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserData @Inject constructor(@ApplicationContext private val context: Context) {

    private val sp = context.getSharedPreferences(Constant.securePrefName, Context.MODE_PRIVATE)
    private val editor = sp.edit()

    fun clearAllPreferences() {
        editor.clear().apply()
    }

    private fun put(key:String,v:String){
        editor.putString(key, v).apply()
    }

    private fun get(key:String):String?{
        return sp.getString(key, "")
    }


    var deviceKey: String
        get() = sp.getString("deviceKey", "") ?: ""
        set(v) = editor.putString("deviceKey", v).apply()

    var isProfileFetched: Boolean
        get() = sp.getBoolean("isProfileFetched", false)
        set(v) = editor.putBoolean("isProfileFetched", v).apply()


    var sessionID: String
        get() {
            "session id get: called : ${sp.getString("sessionIDsss", "") ?: ""}".log()
            return sp.getString("sessionIDsss", "") ?: ""
        }
        set(v) {
            editor.putString("sessionIDsss", v).apply()
            "session id changed: called : $v".log()
        }

    var balanceHistory:String
        get() {
            return (get("walletBalance")?:"").ifEmpty { "0" }
        }
        set(v) {
            put("walletBalance",v)
        }

    var walletBalance:String
        get() {
            return (get("walletBalance")?:"").ifEmpty { "0" }
        }
        set(v) {
            put("walletBalance",v)
        }

    var totalCredit:String
        get() {
            return (get("totalCredit")?:"").ifEmpty { "0" }
        }
        set(v) {
            put("totalCredit",v)
        }
    var totalDebit:String
        get() = (get("totalDebit")?:"").ifEmpty { "0" }
        set(v) = put("totalDebit",v)

    var systemFav:String
        get() {
            return sp.getString("systemFav", "") ?: ""
        }
        set(v) {
            editor.putString("systemFav", v).apply()
        }


    val userName: String
        get() = "$fname $lname"


    var deviceToken: String
        get() = sp.getString("device_token", "") ?: ""
        set(value) = editor.putString("device_token", value).apply()

    var deviceType: String
        get() = sp.getString("device_type", "") ?: ""
        set(value) = editor.putString("device_type", value).apply()

    var email: String
        get() = sp.getString("email", "") ?: ""
        set(value) = editor.putString("email", value).apply()

    var fname: String
        get() = sp.getString("fname", "") ?: ""
        set(value) = editor.putString("fname", value).apply()

    var id: String
        get() = sp.getString("id", "") ?: ""
        set(value) = editor.putString("id", value).apply()

    var ipAddress: String
        get() = sp.getString("ip_address", "") ?: ""
        set(value) = editor.putString("ip_address", value).apply()

    var lname: String
        get() = sp.getString("lname", "") ?: ""
        set(value) = editor.putString("lname", value).apply()

    var mobile: String
        get() = sp.getString("mobile", "") ?: ""
        set(value) = editor.putString("mobile", value).apply()

    var oauthProvider: String
        get() = sp.getString("oauth_provider", "") ?: ""
        set(value) = editor.putString("oauth_provider", value).apply()

    var oauthUid: String
        get() = sp.getString("oauth_uid", "") ?: ""
        set(value) = editor.putString("oauth_uid", value).apply()

    var otpMobile: String
        get() = sp.getString("otp_mobile", "") ?: ""
        set(value) = editor.putString("otp_mobile", value).apply()

    var otpVerify: String
        get() = sp.getString("otp_verify", "") ?: ""
        set(value) = editor.putString("otp_verify", value).apply()

    var pass: String
        get() = sp.getString("pass", "") ?: ""
        set(value) = editor.putString("pass", value).apply()

    var referral: String
        get() = sp.getString("referral", "") ?: ""
        set(value) = editor.putString("referral", value).apply()

    var referralCode: String
        get() = sp.getString("referral_code", "") ?: ""
        set(value) = editor.putString("referral_code", value).apply()

    var address1: String
        get() = sp.getString("S_address1", "") ?: ""
        set(value) = editor.putString("S_address1", value).apply()

    var address2: String
        get() = sp.getString("S_address2", "") ?: ""
        set(value) = editor.putString("S_address2", value).apply()

    var city: String
        get() = sp.getString("S_city", "") ?: ""
        set(value) = editor.putString("S_city", value).apply()

    var country: String
        get() = sp.getString("S_country", "") ?: ""
        set(value) = editor.putString("S_country", value).apply()

    var pincode: String
        get() = sp.getString("S_pincode", "") ?: ""
        set(value) = editor.putString("S_pincode", value).apply()

    var state: String
        get() = sp.getString("S_state", "") ?: ""
        set(value) = editor.putString("S_state", value).apply()

    var cartID: String
        get() = sp.getString("cartID", "") ?: ""
        set(value) = editor.putString("cartID", value).apply()


    fun saveLoginData(data: VerifyOtpResponse.Data) {
        deviceToken = data.deviceToken ?: ""
        deviceType = data.deviceType ?: ""
        email = data.email ?: ""
        fname = data.fname ?: ""
        lname = data.lname ?: ""
        id = data.id ?: ""
        ipAddress = data.ipAddress ?: ""
        cartID = data.key ?: ""
        mobile = data.mobile ?: ""
        oauthProvider = data.oauthProvider ?: ""
        oauthUid = data.oauthUid ?: ""
        otpMobile = data.otpMobile ?: ""
        otpVerify = data.otpVerify ?: ""
        pass = data.pass ?: ""
        referral = data.referral ?: ""
        referralCode = data.referralCode ?: ""
        address1 = data.sAddress1 ?: ""
        address2 = data.sAddress2 ?: ""
        city = data.sCity ?: ""
        country = data.sCountry ?: ""
        pincode = data.sPincode ?: ""
        state = data.sState ?: ""
    }

    fun loadData(): VerifyOtpResponse.Data {
        return VerifyOtpResponse.Data(
            deviceToken = deviceToken,
            deviceType = deviceType,
            email = email,
            fname = fname,
            id = id,
            ipAddress = ipAddress,
            key = cartID,
            lname = lname,
            mobile = mobile,
            oauthProvider = oauthProvider,
            oauthUid = oauthUid,
            otpMobile = otpMobile,
            otpVerify = otpVerify,
            pass = pass,
            referral = referral,
            referralCode = referralCode,
            sAddress1 = address1,
            sAddress2 = address2,
            sCity =    city,
            sCountry = country,
            sPincode = pincode,
            sState = state
        )
    }

}