package com.call2owner.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class CommonRequest(
    @SerializedName("uid")
    @Expose
    var uid: String? = "",
    @SerializedName("order_no")
    @Expose
    var orderNo: String? = "",
    @SerializedName("id")
    @Expose
    var id: String? = "",
    @SerializedName("action")
    @Expose
    var action: String? = "",
    @SerializedName("mobile")
    @Expose
    var mobile: String? = "",
    @SerializedName("enterotp")
    @Expose
    var enteredOtp: String? = "",
    @SerializedName("deviceKey")
    @Expose
    var deviceKey: String? = ""
)