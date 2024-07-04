package com.call2owner.custom.otpView

interface OTPListener {
    fun onInteractionListener()

    fun onOTPComplete(otp: String)
}