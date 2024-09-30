package com.call2owner.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import com.call2owner.R
import com.call2owner.databinding.ActivitySplashBinding
import com.call2owner.model.CommonRequest
import com.call2owner.model.CommonResponse
import com.call2owner.model.TokenResponse
import com.call2owner.ui.BaseActivity
import com.call2owner.ui.activity.auth.LoginActivity
import com.call2owner.utils.MyUtil.fitDialog
import com.call2owner.utils.MyUtil.log
import com.call2owner.utils.MyUtil.model
import com.call2owner.utils.animation.Animation.fadeIn
import android.provider.Settings
import com.call2owner.utils.MyUtil
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    private lateinit var binding:ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkUpdate()
        enableEdgeToEdge()
    }

    private fun checkUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(context)

// Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
               showUpdateDialog()
            }else{
                initView()
            }
        }
    }

    private fun showUpdateDialog() {
        fitDialog(context,activity,"Update Available","","Update,","",false,R.raw.update,true){
            finishAffinity()
        }
    }

    private fun initView() {
        saveDeviceID()
        binding.imageView.fadeIn()
        if(appData.authToken.isEmpty()) {
            apiManager.getToken(false,this )
        }else {
            Handler(Looper.getMainLooper()).postDelayed({
                checkLogin()
            },1000)
        }
    }

    private fun saveDeviceID() {
        if(userData.deviceKey.isEmpty()){
            val id = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            id.log()
            userData.deviceKey=id
        }
    }

    private fun checkLogin() {
        start(if(userData.id.trim().isEmpty()) LoginActivity::class.java else MainActivity::class.java)
        finishAffinity()
    }

    override fun onResult(type: String, success: Boolean, response: String) {
        when (type) {
            "tokenID" -> {
                try {
                    if (success) {
                        val resp = response.model(TokenResponse::class.java)
                        appData.authToken = resp?.auth ?: ""
                        checkLogin()
                    } else {
                        val resp = response.model(CommonResponse::class.java)
                        showErrorSnackBar("Unable To Proceed With Token\nPlease Try Again After sometime: ${resp?.message}")
                    }

                } catch (e: Exception) {
                    e.log()
                    showErrorSnackBar("Bad Json Format: ${e.message}")
                }

            }
        }
    }
}