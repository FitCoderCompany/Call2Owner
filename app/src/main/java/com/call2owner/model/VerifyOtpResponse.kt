package com.call2owner.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class VerifyOtpResponse(
    @SerializedName("data")
    @Expose
    var `data`: List<Data?>? = listOf(),
    @SerializedName("message")
    @Expose
    var message: String? = "",
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = 0
) {
    @Keep
    data class Data(
        @SerializedName("date_time")
        @Expose
        var dateTime: String? = "",
        @SerializedName("device_name")
        @Expose
        var deviceName: String? = "",
        @SerializedName("device_token")
        @Expose
        var deviceToken: String? = "",
        @SerializedName("device_type")
        @Expose
        var deviceType: String? = "",
        @SerializedName("email")
        @Expose
        var email: String? = "",
        @SerializedName("fname")
        @Expose
        var fname: String? = "",
        @SerializedName("id")
        @Expose
        var id: String? = "",
        @SerializedName("ip_address")
        @Expose
        var ipAddress: String? = "",
        @SerializedName("lname")
        @Expose
        var lname: String? = "",
        @SerializedName("mobile")
        @Expose
        var mobile: String? = "",
        @SerializedName("oauth_provider")
        @Expose
        var oauthProvider: String? = "",
        @SerializedName("oauth_uid")
        @Expose
        var oauthUid: String? = "",
        @SerializedName("otp_mobile")
        @Expose
        var otpMobile: String? = "",
        @SerializedName("otp_verify")
        @Expose
        var otpVerify: String? = "",
        @SerializedName("pass")
        @Expose
        var pass: String? = "",
        @SerializedName("referral")
        @Expose
        var referral: String? = "",
        @SerializedName("referral_code")
        @Expose
        var referralCode: String? = "",
        @SerializedName("S_address1")
        @Expose
        var sAddress1: String? = "",
        @SerializedName("S_address2")
        @Expose
        var sAddress2: String? = "",
        @SerializedName("S_city")
        @Expose
        var sCity: String? = "",
        @SerializedName("S_country")
        @Expose
        var sCountry: String? = "",
        @SerializedName("S_pincode")
        @Expose
        var sPincode: String? = "",
        @SerializedName("S_state")
        @Expose
        var sState: String? = "",
        @SerializedName("status")
        @Expose
        var status: String? = ""
    )
}