package com.call2owner.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import java.io.Serializable

@Keep
data class ProductResponse(
    @SerializedName("data")
    @Expose
    var `data`: Data? = Data(),
    @SerializedName("message")
    @Expose
    var message: Any? = Any(),
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = 0
) {
    @Keep
    data class Data(
        @SerializedName("image")
        @Expose
        var image: List<String?>? = listOf(),
        @SerializedName("product_info")
        @Expose
        var productInfo: ProductInfo? = ProductInfo()
    ) {
        @Keep
        data class ProductInfo(
            @SerializedName("delivery_desc")
            @Expose
            var deliveryDesc: String? = "",
            @SerializedName("description")
            @Expose
            var description: String? = "",
            @SerializedName("id")
            @Expose
            var id: String? = "",
            @SerializedName("meta_description")
            @Expose
            var metaDescription: String? = "",
            @SerializedName("meta_keywords")
            @Expose
            var metaKeywords: String? = "",
            @SerializedName("meta_title")
            @Expose
            var metaTitle: String? = "",
            @SerializedName("offer_price")
            @Expose
            var offerPrice: String? = "",
            @SerializedName("price")
            @Expose
            var price: String? = "",
            @SerializedName("pro_name")
            @Expose
            var proName: String? = "",
            @SerializedName("short_description")
            @Expose
            var shortDescription: String? = ""
        ):Serializable
    }
}