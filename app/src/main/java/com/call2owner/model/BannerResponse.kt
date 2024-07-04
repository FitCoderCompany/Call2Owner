package com.call2owner.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class BannerResponse(
    @SerializedName("banner")
    @Expose
    var banner: List<String?>? = listOf(),
    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = 0
)