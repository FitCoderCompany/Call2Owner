package com.call2owner.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class AddCartRequest(
    @SerializedName("action")
    @Expose
    var action: String? = "",
    @SerializedName("CartID")
    @Expose
    var cartID: String? = "",
    @SerializedName("price_per_item")
    @Expose
    var pricePerItem: String? = "",
    @SerializedName("product_id")
    @Expose
    var productId: String? = "",
    @SerializedName("product_name")
    @Expose
    var productName: String? = "",
    @SerializedName("qty")
    @Expose
    var qty: String? = "",
    @SerializedName("total_amount")
    @Expose
    var totalAmount: String? = "",
    @SerializedName("uid")
    @Expose
    var uid: String? = ""
)