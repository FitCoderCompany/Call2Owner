package com.call2owner.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class ShippingRequest(
    @SerializedName("action")
    @Expose
    var action: String? = "",
    @SerializedName("address")
    @Expose
    var address: String? = "",
    @SerializedName("CartID")
    @Expose
    var cartID: String? = "",
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
    var state: String? = "",
    @SerializedName("total_amount")
    @Expose
    var totalAmount: String? = "",
    @SerializedName("uid")
    @Expose
    var uid: String? = ""
)