package com.call2owner.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class PaymentUrlResponse(
    @SerializedName("message")
    @Expose var message: String? = "",
    @SerializedName("orderID")
    @Expose var orderID: String? = "",
    @SerializedName("statusCode")
    @Expose var statusCode: Int? = 0,
    @SerializedName("upiData")
    @Expose var upiData: String? = ""
)