package com.call2owner.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class ActivateCardRequest(
    @SerializedName("action")
    @Expose
    var action: String? = "",
    @SerializedName("cardno")
    @Expose
    var cardno: String? = "",
    @SerializedName("email")
    @Expose
    var email: String? = "",
    @SerializedName("name")
    @Expose
    var name: String? = "",
    @SerializedName("phone")
    @Expose
    var phone: String? = "",
    @SerializedName("Vehicle_No")
    @Expose
    var vehicleNo: String? = ""
)