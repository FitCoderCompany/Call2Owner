package com.call2owner.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import java.io.Serializable

@Keep
data class HistoryResponse(
    @SerializedName("message")
    @Expose
    var message: String? = "",
    @SerializedName("OrderList")
    @Expose
    var orderList: ArrayList<Order?>? = arrayListOf(),
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = 0,
    @SerializedName("Total")
    @Expose
    var total: Int? = 0
) {
    @Keep
    data class Order(
        @SerializedName("E-mail")
        @Expose
        var eMail: String? = "",
        @SerializedName("mobile")
        @Expose
        var mobile: String? = "",
        @SerializedName("OrderDate")
        @Expose
        var orderDate: String? = "",
        @SerializedName("Orderno")
        @Expose
        var orderno: String? = "",
        @SerializedName("ProductName")
        @Expose
        var productName: String? = "",
        @SerializedName("Status")
        @Expose
        var status: String? = "",
        @SerializedName("Total_amount")
        @Expose
        var totalAmount: String? = ""
    ):Serializable
}