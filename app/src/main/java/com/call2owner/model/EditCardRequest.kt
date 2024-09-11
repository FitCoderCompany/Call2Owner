package com.call2owner.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class EditCardRequest(
    @SerializedName("action")
    @Expose
    var action: String? = "",
    @SerializedName("cardno")
    @Expose
    var cardno: String? = "",
    @SerializedName("email")
    @Expose
    var email: String? = "",
    @SerializedName("phone")
    @Expose
    var phone: String? = ""
)