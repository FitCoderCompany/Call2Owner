package com.call2owner.network
import com.call2owner.BuildConfig

object Constant {

    const val sharePrefName = "call2ownerAppData"

    const val securePrefName = "call2ownerUserData"
    const val TAG = "LOG CODER"
    val a: Boolean = BuildConfig.DEBUG


    const val qrCodeImage="https://www.call2owner.com/upload_img/cart_img/img_1_665975618a7204.27364668.jpg"



    const val contactUS="https://www.wltpe.com/Contact"
    const val aboutUs="https://www.wltpe.com/About"
    const val contact="https://www.wltpe.com/Contact"
    const val privacyPolicy="https://www.wltpe.com/Privacy_policy"
    const val termsCondition="https://www.wltpe.com/Terms_conditions"
    const val instagram = "https://www.instagram.com/wltpe_official/"
    const val linkedin = "https://www.linkedin.com/company/wltpe-official/"
    const val twitter = "https://x.com/wltpe_official"
    const val facebook = "https://www.facebook.com/wltpeofficial"
    const val youtube = "https://www.youtube.com/@wltpe_official"


//    https://call2owner.com/modal/login

    const val baseUrl = "https://call2owner.com/"
 
    const val auth = "modal/GetAuthKey"
    const val login = "modal/login"
    const val product = "modal/products"
    const val banner = "modal/media"













    const val loginVerifyOtp = "api/v1/UserManager/customer/verify-otp/"
    const val resendOtp = "api/v1/UserManager/customer/ResendOtp/"

    const val generateToken = "api/v1/UserManager/GenerateToken/"

    const val category = "api/v1/recharge/circle/service-category/get-all/"

    const val updateFav = "api/v1/recharge/user/favorite/add/"

    const val recommendedFav = "api/v1/recharge/user/favorite/get-all/"



    const val serviceDetails = "rechargeRequest/v1/recharge/rechargeRequest/get-plan-info-detail/"

    const val prepaidOperatorPlan = "rechargeRequest/v1/recharge/rechargeRequest/get-operator-plan-offer/"

    const val history = "rechargeRequest/v1/recharge/rechargeRequest/get-all/"


    const val prepaidRecharge="rechargeRequest/v1/recharge/rechargeRequest/initiate/"
    const val cashbackAmount="rechargeRequest/v1/recharge/rechargeRequest/initiate-calculate-amount/"

//    pg
    const val pgInitiateUrl = "api/GatewayResponse/initiate/"

//History



//    Wallet

    const val wallet="api/v1/recharge/user/wallet-balance/"

    const val walletHistory="api/v1/recharge/user/wallet-history/"

    const val getOrderIdForWallet="api/GatewayResponse/initiate-wallet/"

    const val addWallet="/api/GatewayResponse/wallet-transacton/"


}