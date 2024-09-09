package com.call2owner.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class CartEditRequest(
    @SerializedName("action")
    @Expose
    var action: String? = "",
    @SerializedName("id")
    @Expose
    var id: String? = "",
    @SerializedName("price_per_item")
    @Expose
    var pricePerItem: String? = "",
    @SerializedName("qty")
    @Expose
    var qty: String? = "",
    @SerializedName("total_amount")
    @Expose
    var totalAmount: String? = ""
)