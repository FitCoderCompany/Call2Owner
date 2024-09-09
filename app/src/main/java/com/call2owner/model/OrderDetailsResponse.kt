package com.call2owner.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import java.io.Serializable

@Keep
data class OrderDetailsResponse(
    @SerializedName("message")
    @Expose
    var message: String? = "",
    @SerializedName("OrderList")
    @Expose
    var orderList: ArrayList<Order?>? = arrayListOf(),
    @SerializedName("ShippingInfo")
    @Expose
    var shippingInfo: ShippingInfo? = ShippingInfo(),
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = 0,
    @SerializedName("Total")
    @Expose
    var total: Int? = 0
):Serializable {
    @Keep
    data class Order(
        @SerializedName("id")
        @Expose
        var id: String? = "",
        @SerializedName("image")
        @Expose
        var image: String? = "",
        @SerializedName("PricePerItem")
        @Expose
        var pricePerItem: String? = "",
        @SerializedName("ProductName")
        @Expose
        var productName: String? = "",
        @SerializedName("qty")
        @Expose
        var qty: String? = "",
        @SerializedName("Total")
        @Expose
        var total: String? = ""
    ):Serializable

    @Keep
    data class ShippingInfo(
        @SerializedName("Address")
        @Expose
        var address: String? = "",
        @SerializedName("City")
        @Expose
        var city: String? = "",
        @SerializedName("Country")
        @Expose
        var country: String? = "",
        @SerializedName("Customer Name")
        @Expose
        var customerName: String? = "",
        @SerializedName("E-mail")
        @Expose
        var eMail: String? = "",
        @SerializedName("Mobile")
        @Expose
        var mobile: String? = "",
        @SerializedName("Payment Mode")
        @Expose
        var paymentMode: String? = "",
        @SerializedName("Payment Status")
        @Expose
        var paymentStatus: String? = "",
        @SerializedName("Pincode")
        @Expose
        var pincode: String? = "",
        @SerializedName("State")
        @Expose
        var state: String? = ""
    ):Serializable
}