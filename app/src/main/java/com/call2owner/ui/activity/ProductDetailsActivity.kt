package com.call2owner.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.view.View
import com.call2owner.R
import com.call2owner.databinding.ActivityProductDetailsBinding
import com.call2owner.model.AddCartRequest
import com.call2owner.model.CommonRequest
import com.call2owner.model.CommonResponse
import com.call2owner.model.ProductResponse
import com.call2owner.network.Constant
import com.call2owner.ui.BaseActivity
import com.call2owner.ui.activity.auth.LoginActivity
import com.call2owner.utils.MyUtil
import com.call2owner.utils.MyUtil.capWord
import com.call2owner.utils.MyUtil.changeText
import com.call2owner.utils.MyUtil.doubleCurrency
import com.call2owner.utils.MyUtil.log
import com.call2owner.utils.MyUtil.model
import com.call2owner.utils.MyUtil.setImage
import com.call2owner.utils.MyUtil.toast

class ProductDetailsActivity : BaseActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    private val productID="productID"
    private val addCartID="addCartID"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getProductDetails()
    }

    @Suppress("DEPRECATION")
    private fun getProductDetails() {
        val product= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("data",ProductResponse.Data.ProductInfo::class.java)
        } else {
            intent.getSerializableExtra("data") as ProductResponse.Data.ProductInfo
        }

        apiManager.makeRequest(productID,true,"Fetching Details",myApiService.product( CommonRequest(id = product?.id, action = "ProductView")),this)
    }

    @SuppressLint("SetTextI18n")
    private fun setProductData(data:String){
        val resp=data.model(ProductResponse::class.java)
        binding.productCard.visibility= View.VISIBLE
        binding.qrImage.setImage(resp?.data?.image?.last()?:"")
        binding.apply {
            resp?.data?.let {
                productName.text=( it.productInfo?.proName?:"").capWord()
                price.text= (it.productInfo?.price?:"0.0").doubleCurrency()
                price.paintFlags=offerPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                offerPrice.text= (it.productInfo?.offerPrice?:"0.0").doubleCurrency()

                cardInfo.text=it.productInfo?.description?:""
                shippingInfo.text=it.productInfo?.deliveryDesc?:""

                quantity.apply {
                    count.text="1"
                    placeOrder.changeText("Place Order ( ₹${   (it.productInfo?.offerPrice?:"0.0").toDouble() * (count.text.toString().toInt()) })")
                    plus.setOnClickListener{_->
                        count.text = ((count.text.toString().toInt())+1).toString()
                        placeOrder.changeText("Place Order ( ₹${   (it.productInfo?.offerPrice?:"0.0").toDouble() * (count.text.toString().toInt()) })")
                    }
                    minus.setOnClickListener{_->
                        if((count.text?.toString()?:"1").toInt()>1) {
                            count.text = ((count.text.toString().toInt())-1).toString()
                            placeOrder.changeText("Place Order ( ₹${   (it.productInfo?.offerPrice?:"0.0").toDouble() * (count.text.toString().toInt()) })")
                        }
                    }

                    placeOrder.setOnClickListener{_->
                        if(userData.id.trim().isEmpty()){
                            val d= MyUtil.fitDialog(context, activity, "", "Please login to continue", "Login", "Cancel", false, R.raw.login, true){
                                start(LoginActivity::class.java)
                            }
                            d.second.cancel.setOnClickListener{
                                finish()
                            }
                        }else{

                            val req=AddCartRequest(
                                action = "checkout",
                                cartID = userData.cartID,
                                pricePerItem = it.productInfo?.offerPrice?:"0.0",
                                productId = it.productInfo?.id?:"",
                                productName = it.productInfo?.proName?:"",
                                qty = count.text?.toString()?:"1",
                                totalAmount = ((it.productInfo?.offerPrice?:"0.0").toDouble() * (count.text.toString().toInt()) ).toString(),
                                uid = userData.id
                            )
                            apiManager.makeRequest(addCartID,true,"Adding to Cart",myApiService.addCart(req),this@ProductDetailsActivity)
                        }
                    }
                }
            }
        }
    }

    override fun onResult(type: String, success: Boolean, response: String) {
        when(type){
            productID->{
                try {
                    if(success)
                        setProductData(response)
                    else{
                        val resp=response.model( CommonResponse::class.java)
                        showErrorSnackBar(resp?.message)
                    }
                }catch (e:Exception){
                    e.log()
                    showErrorSnackBar("Bad Format json: ${e.message}")
                }
            }
            addCartID->{
                try {
                    val resp=response.model( CommonResponse::class.java)
                    when (resp?.statusCode) {
                        200 -> {
                            toast("Added to Cart")
                            Constant.buyed=true
                            finish()
                        }
                        302 -> {
                            toast(resp.message?:"Product Already in Cart")
                            Constant.buyed=true
                            finish()
                        }
                        else -> {
                            showErrorSnackBar(resp?.message)
                        }
                    }
                }catch (e:Exception){
                    e.log()
                    showErrorSnackBar("Bad Format json: ${e.message}")
                }
            }
        }
    }
}