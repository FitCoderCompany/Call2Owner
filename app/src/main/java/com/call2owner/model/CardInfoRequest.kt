package com.call2owner.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class CardInfoRequest(
    @SerializedName("action")
    @Expose
    var action: String? = "",
    @SerializedName("orderID")
    @Expose
    var orderID: String? = "",
    @SerializedName("uid")
    @Expose
    var uid: String? = ""
)