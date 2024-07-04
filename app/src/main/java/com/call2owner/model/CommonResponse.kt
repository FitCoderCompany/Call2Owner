package com.call2owner.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class CommonResponse(
    @SerializedName("message")
    @Expose
    var message: String? = "",
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = 0
)