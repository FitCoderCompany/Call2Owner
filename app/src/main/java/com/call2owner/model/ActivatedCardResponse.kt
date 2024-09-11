package com.call2owner.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import java.io.Serializable

@Keep
data class ActivatedCardResponse(
    @SerializedName("cardinfo")
    @Expose
    var cardinfo: List<Cardinfo?>? = listOf(),
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = 0,
    @SerializedName("message")
    @Expose
    var message: String? = ""
) {
    @Keep
    data class Cardinfo(
        @SerializedName("activte_date")
        @Expose
        var activteDate: String? = "",
        @SerializedName("cardno")
        @Expose
        var cardNo: String? = "",
        @SerializedName("email")
        @Expose
        var email: String? = "",
        @SerializedName("mobileLink")
        @Expose
        var mobileLink: String? = "",
        @SerializedName("Name")
        @Expose
        var name: String? = "",
        @SerializedName("renewDate")
        @Expose
        var renewDate: String? = "",
        @SerializedName("vehicle_no")
        @Expose
        var vehicleNo: String? = ""
    ):Serializable
}