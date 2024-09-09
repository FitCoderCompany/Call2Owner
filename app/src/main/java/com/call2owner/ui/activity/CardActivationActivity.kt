package com.call2owner.ui.activity

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.call2owner.R
import com.call2owner.custom.CustomTextView
import com.call2owner.databinding.ActivityCardActivationBinding
import com.call2owner.model.ActivateCardRequest
import com.call2owner.model.CardInfoRequest
import com.call2owner.model.CardInfoResponse
import com.call2owner.model.CommonResponse
import com.call2owner.model.HistoryResponse
import com.call2owner.model.OrderDetailsResponse
import com.call2owner.ui.BaseActivity
import com.call2owner.utils.MyUtil
import com.call2owner.utils.MyUtil.log
import com.call2owner.utils.MyUtil.model

class CardActivationActivity : BaseActivity() {
    private lateinit var binding:ActivityCardActivationBinding
    var wholeData: HistoryResponse.Order?=null

    val infoID="InfoID"
    val activateCardID ="activateCardID"
    var cardNo=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCardActivationBinding.inflate(layoutInflater)
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

        getDetails()
        binding.apply {
            activate.setOnClickListener{
                if( validateData(arrayListOf(name,email,vehicleNumber,number))){
                    activateCard()
                }
            }
        }

    }

    private fun activateCard() {
        val req= ActivateCardRequest(
            action = "activate",
            cardno = cardNo,
            email = binding.email.text,
            name = binding.name.text,
            phone = binding.number.text,
            vehicleNo = binding.vehicleNumber.text
        )

        apiManager.makeRequest(activateCardID   ,false,"Fetching Order Details",myApiService.activateCard(req),this)

    }

    fun validateData( data:ArrayList<CustomTextView>): Boolean {
        var good=true
        data.forEach{
            if(it.isEmpty()){
                good=false
                it.setError()
            }
        }
        return good

    }

    private fun getDetails() {
        val req= CardInfoRequest( action = "cardInfo", orderID = wholeData?.orderno?:"",uid=userData.id)
        apiManager.makeRequest(infoID,false,"Fetching Order Details",myApiService.cardApi(req),this)
    }

    override fun onResult(type: String, success: Boolean, response: String) {
        when(type){
            infoID->{
                try{
                    val resp = response.model(CardInfoResponse::class.java)
                    if (success) {
                        cardNo=resp?.card?.cardNo?:""
                    } else {
                        showErrorSnackBar(resp?.message)
                    }
                }catch (e:Exception){
                    e.log()
                    showErrorSnackBar("Bad Format: ${e.message}")
                }
            }

            activateCardID->{
                try{
                    val resp = response.model(CommonResponse::class.java)
                    if (success) {
                        MyUtil.fitDialog(context,activity,"Card Activated Successfully","","","",false,R.raw.success,true){
                            finish()
                        }
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
}