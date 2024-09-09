package com.call2owner.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class UpdateProfileRequest(
    @SerializedName("action")
    @Expose
    var action: String? = "",
    @SerializedName("address")
    @Expose
    var address: String? = "",
    @SerializedName("country")
    @Expose
    var country: String? = "",
    @SerializedName("email")
    @Expose
    var email: String? = "",
    @SerializedName("fname")
    @Expose
    var fname: String? = "",
    @SerializedName("id")
    @Expose
    var id: String? = "",
    @SerializedName("lname")
    @Expose
    var lname: String? = "",
    @SerializedName("pincode")
    @Expose
    var pincode: String? = "",
    @SerializedName("city")
    @Expose
    var city: String? = "",
    @SerializedName("state")
    @Expose
    var state: String? = ""
)