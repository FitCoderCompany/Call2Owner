package com.call2owner.ui.cart

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.call2owner.R
import com.call2owner.databinding.FragmentCartBinding
import com.call2owner.databinding.ItemCartBinding
import com.call2owner.databinding.LayoutAddressBinding
import com.call2owner.model.CartEditRequest
import com.call2owner.model.CartRequest
import com.call2owner.model.CartResponse
import com.call2owner.model.CommonRequest
import com.call2owner.model.CommonResponse
import com.call2owner.model.ShippingRequest
import com.call2owner.ui.BaseFragment
import com.call2owner.ui.activity.MainActivity
import com.call2owner.ui.activity.auth.LoginActivity
import com.call2owner.utils.MyUtil
import com.call2owner.utils.MyUtil.fitDialog
import com.call2owner.utils.MyUtil.getCommaCurrency
import com.call2owner.utils.MyUtil.log
import com.call2owner.utils.MyUtil.model
import com.call2owner.utils.MyUtil.toast
import com.google.android.gms.common.internal.service.Common
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlin.math.ln

class CartFragment : BaseFragment() {
    private lateinit var binding: FragmentCartBinding
    private val cartID: String by lazy { "getCartID"}
    private val removeID: String by lazy { "removeID"}
    private val updateID: String by lazy { "updateID"}
    private val shippingID: String by lazy { "shippingID"}

    private var removedPosition=0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCartBinding.inflate(layoutInflater)
        checkLogin()
        return binding.root
    }

    private fun checkLogin() {
        if(userData.id.trim().isEmpty()){
            val d= fitDialog(requireContext(), requireActivity(), "", "Please login to continue", "Login", "Cancel", false, R.raw.login, true){
                start(LoginActivity::class.java)
            }
            d.second.cancel.setOnClickListener{
                d.first.dismiss()
               ( requireActivity() as MainActivity).navController?.navigate(R.id.navigation_home)
            }
        }else{
            getCarts()
        }
    }
    private fun getCarts( ) {
        val req=CartRequest(action = "checkoutList", cartID = userData.cartID, uid = userData.id)
        apiManager.makeRequest(cartID,true,"Fetching Cart",myApiService.cartList(req),this)
    }

    private fun emptyData(){
        binding.apply {
            emptyLayout.visibility=View.VISIBLE
            recyclerView.visibility=View.GONE
            subTotal.text=getString(R.string.subtotal_, getCommaCurrency(0.0))
            proceed.text= getString(R.string.start_shopping)
            proceed.setOnClickListener{
                (requireActivity() as MainActivity).navController?.navigate(R.id.navigation_home)
            }
        }
    }

    fun updateData(){
        binding.apply {
            val data=(recyclerView.adapter as CartAdapter).getAllData()
            if(data.isEmpty()){
                emptyData()
            }else{
                updateValues(data)

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateValues(data: List<CartResponse.Data?>) {
        var totalAmount=0.0
        data.forEach {
            totalAmount+= (it?.totalAmount?.toDouble()?:0.0)
        }
        binding.apply {
            itemCount.text=data.size.toString()
            proceed.text= getString(R.string.proceed_to_buy_items_, data.size.toString())
            subTotal.text= getString(R.string.subtotal_, MyUtil.getCommaCurrency(totalAmount))

            proceed.setOnClickListener {
                if(userData.fname.isEmpty()) {
                    fitDialog(requireContext(),requireActivity(),"Incomplete profile","Complete your profile to continue","Complete","",false,R.raw.profile,true){
                        requireContext().toast("Enter your address in Profile")
                        (requireActivity() as MainActivity).navController?.navigate(R.id.navigation_profile)
                    }
                }else{
                    val circleSheet = BottomSheetDialog(requireContext(), R.style.FullBottomSheetTheme)
                    val cBinding = LayoutAddressBinding.inflate(layoutInflater)
                    circleSheet.setContentView(cBinding.root)
                    cBinding.apply {
                        price.sub= getCommaCurrency(totalAmount)
                        val deliveryCharge=0.00 // to be changed
                        delivery.sub= getCommaCurrency(deliveryCharge)
                        totalAmount+=deliveryCharge
                        total.sub= getCommaCurrency(totalAmount)
                        profileAddress.text="${userData.address1}, ${userData.city}, ${userData.state}, ${userData.country}-${userData.pincode}"
                        changeAddress.setOnClickListener{

                        }

                        startShipping.setOnClickListener{
                            circleSheet.dismiss()
                            startShipping(totalAmount.toString())
                        }
                    }
                    circleSheet.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    circleSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    circleSheet.show()

                }
            }

        }
    }

    private fun startShipping(amount:String) {
        binding.apply {
            val req=ShippingRequest(
                action = "shipping",
                address = "${userData.address1} ${userData.address2}",
                cartID = userData.cartID,
                city = userData.city,
                country = userData.country,
                email = userData.email,
                fname = userData.fname,
                lname = userData.lname,
                mobile = userData.mobile,
                pincode = userData.pincode,
                state = userData.state,
                totalAmount = amount,
                uid = userData.id
            )

            apiManager.makeRequest(shippingID,true,"Booking Card",myApiService.shipping(req),this@CartFragment)

        }

    }

    private fun setCartData(data: ArrayList<CartResponse.Data?>?) {
        binding.apply {
            if(data.isNullOrEmpty()){
                emptyData()
            }else{
                emptyLayout.visibility=View.GONE
                recyclerView.visibility=View.VISIBLE
                val adapter=CartAdapter(data)
                recyclerView.adapter=adapter
                updateValues(data)
            }
        }
    }

    fun updateDB(item: CartResponse.Data?) {
        val req=CartEditRequest("cartEdit", item?.id, item?.pricePerItem, item?.qty, item?.totalAmount)
        apiManager.makeRequest(updateID,false,"",myApiService.editCart(req),this)
    }

    inner class CartAdapter(val data: ArrayList<CartResponse.Data?>):RecyclerView.Adapter<CartAdapter.Holder>(){

        inner class Holder(val binding:ItemCartBinding):RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
             return Holder(ItemCartBinding.inflate(LayoutInflater.from(p0.context),p0,false))
        }

        override fun getItemCount()=data.size

        override fun onBindViewHolder(h: Holder, p: Int) {
            h.binding.apply {
                val item=data[p]
                item?.let {
                    name.text=it.productName
                    price.text= getString(R.string.price_, MyUtil.getCommaCurrency(it.pricePerItem))
                    amount.text= getString(R.string.total_, MyUtil.getCommaCurrency(it.totalAmount))
                    quantity.apply {
                        count.text=it.qty
                        plus.setOnClickListener{
                            data[p]?.qty=((data[p]?.qty?.toInt()?:0)+1).toString()
                            data[p]?.totalAmount=( (data[p]?.qty?.toDouble()?:0.0) * (data[p]?.pricePerItem?.toDouble()?:0.0) ).toString()
                            notifyItemChanged(p)
                            updateData()
                            updateDB(data[p])
                        }
                        minus.setOnClickListener{
                            if(data[p]?.qty?.toInt()!=1) {
                                data[p]?.qty=((data[p]?.qty?.toInt()?:2)-1).toString()
                                data[p]?.totalAmount=( (data[p]?.qty?.toDouble()?:0.0) * (data[p]?.pricePerItem?.toDouble()?:0.0) ).toString()
                                notifyItemChanged(p)
                                updateData()
                                updateDB(data[p])
                            }else if(data[p]?.qty?.toInt()==1){
                                deleteItem(item,p)
                            }
                        }
                    }
                    remove.setOnClickListener{
                        deleteItem(item,p)
                    }
                }
            }
        }


        fun getAllData(): List<CartResponse.Data?> {
            return data
        }
    }

    private fun deleteItem(it: CartResponse.Data?, position : Int) {
        fitDialog(requireContext(),requireActivity(),"Delete!","Are you sure you want to remove this item?","Delete","",true,R.raw.delete,true){
            val req=CommonRequest(action = "remove", id = it?.id)
            removedPosition=position
            apiManager.makeRequest(removeID,true,"Deleting Cart",myApiService.deleteCart(req),this)
        }

    }


    override fun onResult(type: String, success: Boolean, response: String) {
        when(type){
            cartID->{
                try {
                    val resp=response.model(CartResponse::class.java)
                    if(success){
                        setCartData(resp?.data)
                    }else {
                        emptyData()
//                        showErrorSnackBar(resp?.message)
                    }
                }catch (e:Exception){
                    emptyData()
                    e.log()
//                    showErrorSnackBar("Bad Format json: ${e.message}")
                }
            }

            shippingID->{
                try {
                    val resp=response.model(CommonResponse::class.java)
                    if(success){
                        fitDialog(requireContext(),requireActivity(),"Successful","Order Placed Successfully","Continue Shopping","",false,R.raw.success,true){
                            (requireActivity() as MainActivity).navController?.navigate(R.id.navigation_home)
                        }
                    }else{
                        showErrorSnackBar(resp?.message)
                    }
                }catch (e:Exception){
                    e.log()
                    showErrorSnackBar("Bad Format json: ${e.message}")
                }
            }
            removeID->{
                try {
                    val resp=response.model(CommonResponse::class.java)
                    if(success){
                        (binding.recyclerView.adapter as CartAdapter).data.removeAt(removedPosition)
                        (binding.recyclerView.adapter as CartAdapter).notifyItemRemoved(removedPosition)
                        updateData()
                    }else {
                        showErrorSnackBar(resp?.message)
                    }
                }catch (e:Exception){
                    e.log()
                    showErrorSnackBar("Bad Format json: ${e.message}")
                }
            }
        }
    }
}