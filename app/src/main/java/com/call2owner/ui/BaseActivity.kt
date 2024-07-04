package com.call2owner.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.call2owner.R
import com.call2owner.network.apis.MyApi
import com.call2owner.utils.AppData
import com.call2owner.utils.UserData
import com.call2owner.network.ApiManager
import com.call2owner.network.NetworkInterface
import com.call2owner.utils.MyUtil
import com.call2owner.utils.MyUtil.showNoInternetDialog
import com.call2owner.utils.MyUtil.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject

@AndroidEntryPoint
open class BaseActivity:AppCompatActivity(),NetworkInterface {
    @Inject
    lateinit var apiManager: ApiManager

    @Inject
    lateinit var userData: UserData

    @Inject
    lateinit var appData: AppData

    @Inject
    lateinit var myApiService: MyApi

    lateinit var context: Context


    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: NetworkCallbackImpl
    var noNetworkAlert: Pair<Button, AlertDialog>?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this

        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkCallback = NetworkCallbackImpl(this)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out,R.color.white)
        }else{
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out )
        }
    }



    fun start(activity:Class<*>){
        val intent=Intent(context,activity)
        startActivity(intent)
    }
    fun showErrorSnackBar(msg: String, btnText: String, btnFun: () -> Unit) {
        context.showSnackBar(window,msg,btnText,btnFun)

    }

    fun showErrorSnackBar(msg: String?=null) {
        context.showSnackBar(window,msg)
    }

    fun showSuccessSnackBar(msg: String?) {
        context.showSnackBar(window,msg)
    }

    inner class NetworkCallbackImpl(private val context: Context) : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            if(noNetworkAlert?.second?.isShowing == true){
                noNetworkAlert?.second?.dismiss()
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            noNetworkAlert= context.showNoInternetDialog(this@BaseActivity)
            noNetworkAlert?.first?.visibility= View.GONE
            noNetworkAlert?.second?.show()
        }
    }

    override fun onResult(type: String, success: Boolean, response: String) { }


    override fun onNoInternet( ) {
         context.showNoInternetDialog(this@BaseActivity)
    }

}