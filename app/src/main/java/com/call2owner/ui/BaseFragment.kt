package com.call2owner.ui

import androidx.fragment.app.Fragment
import com.call2owner.network.ApiManager
import com.call2owner.network.NetworkInterface
import com.call2owner.network.apis.MyApi
import com.call2owner.utils.AppData
import com.call2owner.utils.MyUtil.copyToClipboard
import com.call2owner.utils.MyUtil.getLastStringAfterDot
import com.call2owner.utils.MyUtil.log
import com.call2owner.utils.MyUtil.showNoInternetDialog
import com.call2owner.utils.MyUtil.showSnackBar
import com.call2owner.utils.MyUtil.start
import com.call2owner.utils.UserData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseFragment:Fragment(),NetworkInterface {
    @Inject
    lateinit var appData: AppData

    @Inject
    lateinit var apiManager: ApiManager

    @Inject
    lateinit var userData: UserData

    @Inject
    lateinit var myApiService: MyApi

    override fun onResume() {
        super.onResume()
         ("Fragment ${javaClass.simpleName. getLastStringAfterDot()}").log()
    }

    fun showErrorSnackBar(msg: String, btnText: String, btnFun: () -> Unit) {
        requireContext().showSnackBar(requireActivity().window ,msg,btnText,btnFun)
    }

    fun showErrorSnackBar(msg: String?=null) {
        requireContext().showSnackBar(requireActivity().window ,msg)
    }

    fun showSuccessSnackBar(msg: String?) {
        requireContext().showSnackBar(requireActivity().window,msg)
    }

    fun <T> start(cl: Class<T>) {
        requireActivity().start(requireContext(),cl)
    }

    fun copyToClipboard(text: String) {
        requireContext().copyToClipboard(text)
    }

    override fun onResult(type: String,success:Boolean, response: String) {

    }



    override fun onNoInternet( ) {
        requireContext().showNoInternetDialog(requireActivity())
    }
}