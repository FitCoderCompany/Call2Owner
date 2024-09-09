package com.call2owner.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class ProfileResponse(
    @SerializedName("data")
    @Expose
    var `data`: Data? = Data(),
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = 0
) {
    @Keep
    data class Data(
        @SerializedName("address")
        @Expose
        var address: String? = "",
        @SerializedName("city")
        @Expose
        var city: String? = "",
        @SerializedName("country")
        @Expose
        var country: String? = "",
        @SerializedName("email")
        @Expose
        var email: String? = "",
        @SerializedName("fname")
        @Expose
        var fname: String? = "",
        @SerializedName("lname")
        @Expose
        var lname: String? = "",
        @SerializedName("mobile")
        @Expose
        var mobile: String? = "",
        @SerializedName("pincode")
        @Expose
        var pincode: String? = "",
        @SerializedName("state")
        @Expose
        var state: String? = ""
    )
}