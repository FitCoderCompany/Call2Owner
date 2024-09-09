package com.call2owner.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class CartResponse(
    @SerializedName("data")
    @Expose
    var `data`: ArrayList<Data?>? = arrayListOf(),
    @SerializedName("message")
    @Expose
    var message: String? = "",
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = 0
) {
    @Keep
    data class Data(
        @SerializedName("id")
        @Expose
        var id: String? = "",
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
        var totalAmount: String? = ""
    )
}