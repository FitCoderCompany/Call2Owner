package com.call2owner.ui.home

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.call2owner.R
import com.call2owner.databinding.FragmentHomeBinding
import com.call2owner.model.BannerResponse
import com.call2owner.model.CommonRequest
import com.call2owner.model.CommonResponse
import com.call2owner.model.ProductResponse
import com.call2owner.model.TokenResponse
import com.call2owner.ui.BaseFragment
import com.call2owner.ui.activity.MainActivity
import com.call2owner.ui.activity.ProductDetailsActivity
import com.call2owner.ui.activity.ScanActivity
import com.call2owner.ui.activity.WebActivity
import com.call2owner.ui.contact.ContactActivity
import com.call2owner.utils.MyUtil.capWord
import com.call2owner.utils.MyUtil.doubleCurrency
import com.call2owner.utils.MyUtil.explicitWeb
import com.call2owner.utils.MyUtil.log
import com.call2owner.utils.MyUtil.model
import com.call2owner.utils.MyUtil.setImage
import com.call2owner.utils.bannerSlider.ParallelXViewPagerTransformer
import com.call2owner.utils.bannerSlider.SliderAdapterPager

class HomeFragment : BaseFragment() {

    val productID="productID"
    val productDetails="productDetails"
    val bannerID="bannerID"

    private var sliderAdapter: SliderAdapterPager?=null
//    private val instructionImg = arrayListOf(
//        "https://www.call2owner.com/assets/images/text-with-image.jpg",
//        "https://www.call2owner.com/assets/images/step-2.jpeg",
//        "https://www.call2owner.com/assets/images/bannermobilesize3.jpg"
//    )

    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        setDrawer()
        getAllData()
        binding.scanQr.setOnClickListener{
            start(ScanActivity::class.java)
        }
    }

    private fun setDrawer() {
        binding.apply {
            drawerNav.setNavigationItemSelectedListener {
                it.isChecked=false
                drawerLayout.close()
                when (it.itemId) {
                    R.id.about ->  openImplicitWeb("https://call2owner.com/about-us")
                    R.id.contact -> start(ContactActivity::class.java)
                    R.id.faq ->  openImplicitWeb("https://call2owner.com/frequently-asked-questions")
                    R.id.use -> openImplicitWeb("https://call2owner.com/how-to-use")

                    R.id.fb -> requireContext().explicitWeb("https://www.facebook.com/call2owner")
                    R.id.x -> requireContext().explicitWeb("https://x.com/Call2Owner")
                    R.id.instagram -> requireContext().explicitWeb("https://www.instagram.com/call2owner/")
                    R.id.youtube -> requireContext().explicitWeb("https://www.youtube.com/@call2owner")
                    R.id.linkedin -> requireContext().explicitWeb("https://www.linkedin.com/company/call2owner/")
                }
                true
            }

            settings.setOnClickListener {
                drawerLayout.open()
            }
        }
    }

    private fun openImplicitWeb(web: String) {
        Intent(requireContext(),WebActivity::class.java).run{
            putExtra("url",web)
            startActivity(this)
        }
    }

    private fun getAllData() {

        if(appData.authToken.isEmpty()) {
            apiManager.getToken(true,this )
        }else {
            val banner=appData.bannerImages
            if(banner.isNotEmpty())
                setBanners(banner)

            apiManager.makeRequest(bannerID, false, "", myApiService.banner(CommonRequest(action = "banner")), this)
            val products = appData.productDetails
            if (products.isNotEmpty())
                setProductData(products)

            apiManager.makeRequest(productID, products.isEmpty(), "", myApiService.product(CommonRequest(action = "product")), this)
        }
    }

    private fun setBanners(response: String) {
        binding.apply {
            val bannerUrlList=response.model(BannerResponse::class.java)?.banner?: listOf()
            tabLayout.setupWithViewPager(viewPager)
            viewPager.apply {
                sliderAdapter= SliderAdapterPager(requireContext(),bannerUrlList,layoutInflater, Handler(Looper.getMainLooper()))
                adapter= sliderAdapter
                clipToPadding=false
                pageMargin=24
                setPageTransformer(true , ParallelXViewPagerTransformer( ))
                sliderAdapter?.startAutoScroll(this)
            }
        }
    }

    private fun setProductData(data:String){
        val resp=data.model(ProductResponse::class.java)
        binding.productCard.visibility=View.VISIBLE
        binding.qrImage.setImage(resp?.data?.image?.last()?:"")
        binding.apply {
            resp?.data?.let {
                productName.text=( it.productInfo?.proName?:"").capWord()
                price.text= (it.productInfo?.price?:"0.0").doubleCurrency()
                price.paintFlags=offerPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                offerPrice.text= (it.productInfo?.offerPrice?:"0.0").doubleCurrency()
                details.setOnClickListener{_->
                    Intent(requireContext(), ProductDetailsActivity::class.java).run{
                        putExtra("data",it.productInfo)
                        startActivity(this)
                    }
                }
//                desc.text=it.productInfo?.shortDescription?:""
            }
        }
    }

    override fun onResult(type: String, success: Boolean, response: String) {
        when(type){
            "tokenID"->{
                try {
                    if(success){
                        val resp=response.model(TokenResponse::class.java)
                        appData.authToken=resp?.auth?:""
                        getAllData()
                    }else{
                        val resp=response.model(CommonResponse::class.java)
                        showErrorSnackBar("Unable To Proceed With Token\nPlease Try Again After sometime: ${resp?.message}")
                    }

                }catch (e:Exception){
                    e.log()
                    showErrorSnackBar("Bad Json Format: ${e.message}")
                }

            }

            bannerID->{
                if(success){
                    if(appData.bannerImages.isEmpty()){
                        setBanners(response)
                    }
                    appData.bannerImages=response
                }else{
                    binding.bannerLayout.visibility=View.GONE
                }
            }

            productID->{
                if(success){
                    if(appData.productDetails.isEmpty()){
                        setProductData(response)
                    }
                    appData.productDetails=response

                }else{
                    binding.productCard.visibility=View.GONE
                }
            }

            productDetails->{

            }

        }


    }
    override fun onDestroy() {
        super.onDestroy()
        sliderAdapter?.stopAutoScroll()
    }
}