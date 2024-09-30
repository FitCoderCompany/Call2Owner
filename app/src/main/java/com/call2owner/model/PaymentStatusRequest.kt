package com.call2owner.model

data class PaymentStatusRequest(val action:String?="checkResponse", var orderID: String? = "",var uid: String? = "",var CartID:String?="")
