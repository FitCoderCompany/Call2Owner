package com.call2owner.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class CartRequest(
    @SerializedName("action")
    @Expose
    var action: String? = "",
    @SerializedName("CartID")
    @Expose
    var cartID: String? = "",
    @SerializedName("uid")
    @Expose
    var uid: String? = ""
)