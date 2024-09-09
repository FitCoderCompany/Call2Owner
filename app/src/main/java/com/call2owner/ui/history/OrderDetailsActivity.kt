package com.call2owner.ui.history

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.call2owner.databinding.ActivityOrderDetailsBinding
import com.call2owner.databinding.ItemOrderDetailsBinding
import com.call2owner.model.CommonRequest
import com.call2owner.model.HistoryResponse
import com.call2owner.model.OrderDetailsResponse
import com.call2owner.ui.BaseActivity
import com.call2owner.ui.activity.CardActivationActivity
import com.call2owner.utils.MyUtil.log
import com.call2owner.utils.MyUtil.model
import com.call2owner.utils.MyUtil.setImage

class OrderDetailsActivity : BaseActivity() {
    private lateinit var binding:ActivityOrderDetailsBinding
    private val detailID="detailID"
    var wholeData: HistoryResponse.Order?=null
    var shipping: OrderDetailsResponse.ShippingInfo?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        wholeData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("data",HistoryResponse.Order::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("data") as HistoryResponse.Order
        }

        getDetails(wholeData?.orderno?:"")


    }

    private fun getDetails(orderNo: String) {
        val req= CommonRequest( action = "ItemList", orderNo = orderNo)
        apiManager.makeRequest(detailID,true,"Fetching Order Details",myApiService.orderHistory(req),this)
    }

    @SuppressLint("SetTextI18n")
    private fun setDetails(resp: OrderDetailsResponse?) {
        val data=resp?.orderList
        shipping = resp?.shippingInfo
        binding.apply {
            number.sub=shipping?.mobile?:""
            email.sub=shipping?.eMail?:""
            status.sub=wholeData?.status?:""
            date.sub=wholeData?.orderDate?:""
            paymentMethod.sub=shipping?.paymentMode?:""
            paymentStatus.sub=shipping?.paymentStatus?:""
            profileAddress.text=("${ shipping?.address ?: "" }, ${ shipping?.city ?: "" }, ${ shipping?.state ?: "" } - ${ shipping?.pincode ?: "" }, ${ shipping?.country ?: "" } ").trim().ifEmpty {
                addressTxt.visibility= View.GONE
                profileAddress.visibility= View.GONE
                ""
            }
        }
        if (!data.isNullOrEmpty()) {
            binding.recyclerView.adapter=Adapter(data)
        }
    }


    override fun onResult(type: String, success: Boolean, response: String) {
        when(type){
            detailID->{
                try{
                    val resp = response.model(OrderDetailsResponse::class.java)
                    if (success) {
                        setDetails(resp)

                    } else {
                        showErrorSnackBar(resp?.message)
                    }
                }catch (e:Exception){
                    e.log()
                    showErrorSnackBar("Bad Format: ${e.message}")
                }
            }
        }
    }


    inner class Adapter(val data:ArrayList<OrderDetailsResponse.Order?>):RecyclerView.Adapter<Adapter.Holder>(){
        inner class Holder(val binding: ItemOrderDetailsBinding):RecyclerView.ViewHolder(binding.root)
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
            return Holder(ItemOrderDetailsBinding.inflate(LayoutInflater.from(p0.context),p0,false))
        }
        override fun getItemCount()=data.size
        override fun onBindViewHolder(holder: Holder, p1: Int) {
            val item=data[p1]
            holder.binding.apply {
                qrImage.setImage(item?.image)
                productName.text=item?.productName?:""
                quantity.sub=item?.qty?:""
                priceItem.sub=item?.pricePerItem?:""
                totalAmount.sub=item?.total?:""

                when(wholeData?.status?.contains("Delivered",true)){
                    true->{
                        activate.visibility=View.VISIBLE
                        activate.setOnClickListener{
                            Intent(context, CardActivationActivity::class.java).run {
                                putExtra("wholeData",wholeData)
                                putExtra("shippingDetails",shipping)
                                putExtra("data",item)
                            }
                        }
                    }
                    false->{
                        activate.visibility=View.GONE
                    }
                    else->{
                        activate.visibility=View.GONE
                    }
                }
            }
        }
    }
}