package com.call2owner.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class TokenResponse(
    @SerializedName("auth")
    @Expose
    var auth: String? = "",
    @SerializedName("message")
    @Expose
    var message: String? = ""
)