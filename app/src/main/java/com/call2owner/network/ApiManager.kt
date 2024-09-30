package com.call2owner.network

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.call2owner.R
import com.call2owner.databinding.LayoutLoadingBinding
import com.call2owner.model.CommonResponse
import com.call2owner.model.TokenResponse
import com.call2owner.utils.UserData
import com.call2owner.network.apis.MyApi
import com.call2owner.ui.activity.auth.LoginActivity
import com.call2owner.utils.AppData
import com.call2owner.utils.MyUtil
import com.call2owner.utils.MyUtil.hideKeyboard
import com.call2owner.utils.MyUtil.log
import com.call2owner.utils.MyUtil.model
import com.call2owner.utils.MyUtil.toast
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ApiManager @Inject constructor(@ActivityContext private val context: Context, @ActivityScoped private val activity: Activity) {

    @Inject
    lateinit var appData: AppData

    @Inject
    lateinit var userData: UserData

    @Inject
    lateinit var myApiService: MyApi

    fun makeRequest(type: String, isLoad: Boolean, loaderMessage: String = "", primaryCall: Call<ResponseBody>, listener: NetworkInterface) {
        if(isLoad)
            activity.hideKeyboard()
        if (DetectConnection(context).checkInternetConnection()) {
            val errorMsg: String by lazy { context.getString(R.string.something_wrong) }
            if (isLoad) {
                showLoader(loaderMessage)
            }
            primaryCall.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    hideLoader()
                    val responseCode=response.code()
                    val responseValue =if(responseCode==200){
                        response.body()?.string()
                    }else{
                        response.errorBody()?.string()
                    }
                    "Response Code for $type: $responseCode".log()
                    "Response Param for $type: $responseValue".log()
                    if(responseCode==401){
                        val o= Intent(context, LoginActivity::class.java)
                        activity.startActivity(o)
                        context.toast("You are login in another device")
                        activity.finishAffinity()
                    }else{
                        listener.onResult(type,  responseCode==200, responseValue ?: "errorMsg")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    hideLoader()
                    (t.localizedMessage?:"empty Message").log()
                    listener.onResult(type,false,  t.localizedMessage ?: errorMsg)
                }
            })
        } else {
            listener.onNoInternet( )
        }
    }

    fun getToken(isLoad: Boolean, listener: NetworkInterface){
        if (DetectConnection(context).checkInternetConnection()) {
            if (isLoad) showLoader("Generating Token")
            val type="tokenID"
            myApiService.getToken().enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    hideLoader()
                    val responseCode=response.code()
                    val responseValue =if(responseCode==200){
                        response.body()?.string()
                    }else{
                        response.errorBody()?.string()
                    }
                    "Response Code for $type: $responseCode".log()
                    "Response Param for $type: $responseValue".log()

                    if(responseCode==401){
                        val o= Intent(context, LoginActivity::class.java)
                        activity.startActivity(o)
                        context.toast("You are login in another device")
                        activity.finishAffinity()
                    }else{
                        listener.onResult(type,  responseCode==200, responseValue ?: "errorMsg")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    hideLoader()
                    t.localizedMessage?:"empty Message".log()
                }
            })
        } else{
            context.toast("Connect to network")
        }
    }
    private var loaderAlert: AlertDialog? = null

    private fun initLoader(loaderMessage: String) {
        val alertDialog = AlertDialog.Builder(context)
        val lb = LayoutLoadingBinding.inflate(activity.layoutInflater)
        if (loaderMessage.isEmpty()) {
            lb.msg.visibility = View.GONE
        } else {
            lb.msg.run {
                visibility = View.VISIBLE
                text = loaderMessage
            }
        }
        alertDialog.setView(lb.root)
        alertDialog.setCancelable(false)
        loaderAlert = alertDialog.create()
        loaderAlert?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun showLoader(loaderMessage: String) {
        initLoader(loaderMessage)
        try {
            if (loaderAlert?.isShowing != true)
                loaderAlert?.show()
        } catch (e: Exception) {
            showLoader(loaderMessage)
        }
    }

    fun hideLoader() {
        try {
            if (loaderAlert?.isShowing == true) {
                loaderAlert?.dismiss()
            }
        } catch (e: Exception) {
            e.log()
        } finally {
            loaderAlert?.onBackPressed()
            loaderAlert = null
        }
    }
}