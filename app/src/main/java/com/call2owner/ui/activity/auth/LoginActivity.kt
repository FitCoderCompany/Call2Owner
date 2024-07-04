package com.call2owner.ui.activity.auth

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.call2owner.R
import com.call2owner.custom.otpView.OTPListener
import com.call2owner.databinding.ActivityLoginBinding
import com.call2owner.model.CommonRequest
import com.call2owner.model.CommonResponse
import com.call2owner.model.VerifyOtpResponse
import com.call2owner.ui.BaseActivity
import com.call2owner.ui.activity.MainActivity
import com.call2owner.utils.MyUtil.log
import com.call2owner.utils.MyUtil.model
import com.google.android.gms.auth.api.phone.SmsRetriever

class LoginActivity : BaseActivity() {
    private lateinit var binding:ActivityLoginBinding

    private val loginID="LoginID"
    private val resendID="resendID"
    private val verify="verify"
    private lateinit var smsBroadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        registerSmsBroadcastReceiver()
    }

    private fun initView() {
        binding.apply {
            mobileNumber.getEditView().doAfterTextChanged {
                it?.let {
                    binding.sendOTP.isEnabled=it.length==10
                }
            }

            sendOTP.setOnClickListener{
                if(mobileNumber.text.length==10){
                    startLogin()
                }else{
                    mobileNumber.setError()
                }
            }
        }
    }

    private fun startLogin() {
        val req= CommonRequest(action = "login", mobile = binding.mobileNumber.text)
        apiManager.makeRequest(loginID,true,"Sending OTP",myApiService.login(req),this)
    }

    private fun showOTPLayout() {
        binding.apply {
            otpNumber.text= getString(R.string.otp_has_been_sent_to, mobileNumber.text)
            otpLayout.visibility=View.VISIBLE
            sendOTP.visibility=View.GONE
            mobileNumber.apply {
                setEnable(false)
                setActions("Edit")
                getActionBtn().setOnClickListener{
                    clearText()
                    setEnable(true)
                    setActions("")
                    otpLayout.visibility=View.GONE
                    sendOTP.visibility=View.VISIBLE
                    stopSmsUserConsent()
                }
            }

            otpView.otpListener = object : OTPListener {
                override fun onInteractionListener() {}
                override fun onOTPComplete(otp: String) {
                    callVerifyOTP(otp)
                }
            }
            startCountDownToResend()
        }
    }

    private fun callVerifyOTP(otp: String) {
        val req=CommonRequest(action = "verify", mobile = binding.mobileNumber.text, enteredOtp = otp)
        apiManager.makeRequest(verify,true,"Verifying OTP",myApiService.login(req),this)
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun registerSmsBroadcastReceiver() {
        smsBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (SmsRetriever.SMS_RETRIEVED_ACTION == intent?.action) {
                    val extras = intent.extras
                    @Suppress("DEPRECATION")
                    val consentIntent = extras?.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                    try {
                        consentIntent?.let { smsConsentLauncher.launch(it) }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(smsBroadcastReceiver, intentFilter, RECEIVER_EXPORTED)
        }else{
            registerReceiver(smsBroadcastReceiver, intentFilter)
        }
    }

    val  smsConsentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val message = result.data?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
            message?.let {
                extractOtpFromMessage(it)
                ("Message received: $it").log()
            }
        } else {
            showErrorSnackBar("Consent Canceled")
            ("Consent canceled").log()
        }
    }

    private fun extractOtpFromMessage(msg: String) {
        if (msg.isNotEmpty()) {
            val otpPattern = "\\b\\d{4}\\b".toRegex()
            val matchResult = otpPattern.find(msg)
            ("Message received here: ${matchResult?.value}").log()

            binding.otpView.setOTP(matchResult?.value?:"")
        }else{
            showErrorSnackBar("Unable to fetch OTP")
        }
    }
    private fun startSmsUserConsent() {
        val client  = SmsRetriever.getClient(this)
        client.startSmsUserConsent(null)
    }
    private fun startCountDownToResend() {
        startSmsUserConsent()
        object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.resend.text = getString(R.string.resend_msg, millisUntilFinished / 1000)
            }

            override fun onFinish() {
                binding.resend.apply {
                    text=getString(R.string.resend)
                    setOnClickListener {
                        callResendOTP()
                        binding.otpView.setOTP("")
                        startCountDownToResend()

                    }
                }
            }
        }.start()
    }

    private fun callResendOTP() {
        val req=CommonRequest(action = "resend", mobile = binding.mobileNumber.text)
        apiManager.makeRequest(resendID,true,"Sending OTP Again",myApiService.login(req),this)

    }

    private fun stopSmsUserConsent() {
        try {
            unregisterReceiver(smsBroadcastReceiver)
        }catch (e:Exception){
            e.log()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
       stopSmsUserConsent()
    }
    override fun onResult(type: String,success:Boolean, response: String) {
        when(type){
            loginID,resendID->{
                val resp=response.model(CommonResponse::class.java)
                if(success){
                    showOTPLayout()
                }else{
                    showErrorSnackBar(resp?.message)
                }
            }

            verify->{
                val resp=response.model(VerifyOtpResponse::class.java)
                if(success){
                    resp?.data?.get(0)?.let {
                        userData.saveLoginData(it)
                        finish()
                    }
                }else{
                    binding.otpView.setOTP("")
                    binding.otpView.showError()
                    binding.otpError.text= getString(R.string.invalid_otp)
                    showErrorSnackBar(resp?.message)
                }
            }
        }
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        binding.apply {
            if(otpLayout.isVisible){
                mobileNumber.apply {
                    clearText()
                    setEnable(true)
                    setActions("")
                    otpLayout.visibility=View.GONE
                    sendOTP.visibility=View.VISIBLE
                    stopSmsUserConsent()
                }
            } else{
                super.onBackPressed()
            }
        }
    }

}