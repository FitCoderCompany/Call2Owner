package com.call2owner.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class CardInfoResponse(
    @SerializedName("Card")
    @Expose
    var card: Card? = Card(),
    @SerializedName("message")
    @Expose
    var message: String? = "",
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = 0
) {
    @Keep
    data class Card(
        @SerializedName("Card_no")
        @Expose
        var cardNo: String? = "",
        @SerializedName("validity")
        @Expose
        var validity: String? = ""
    )
}