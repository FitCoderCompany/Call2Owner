package com.call2owner.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.call2owner.R
import com.call2owner.databinding.ActivityOwnCardBinding
import com.call2owner.databinding.ItemActivatedCardBinding
import com.call2owner.model.ActivatedCardResponse
import com.call2owner.model.CommonRequest
import com.call2owner.ui.BaseActivity
import com.call2owner.utils.MyUtil.explicitWeb
import com.call2owner.utils.MyUtil.model

class OwnCardActivity : BaseActivity() {
    private lateinit var binding:ActivityOwnCardBinding
    private val getOwnCardID="getOwnCardID"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityOwnCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getOwnCardData()
    }

    private fun getOwnCardData() {
        val req=CommonRequest(uid=userData.id,action="ActivateCardList")
        apiManager.makeRequest(getOwnCardID,true,"Getting Activated Card List",myApiService.activatedCard(req),this)
    }

    override fun onResult(type: String, success: Boolean, response: String) {
        when(type){
            getOwnCardID->{
                val resp=response.model(ActivatedCardResponse::class.java)
                if(success){
                    setCards(resp?.cardinfo)
                }else{
                    showErrorSnackBar(resp?.message)
                }
            }
        }
    }

    inner class Adapter(val data: List<ActivatedCardResponse.Cardinfo?>): RecyclerView.Adapter<Adapter.Holder>(){
        inner class Holder(val binding: ItemActivatedCardBinding): RecyclerView.ViewHolder(binding.root)
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
            return Holder(ItemActivatedCardBinding.inflate(LayoutInflater.from(p0.context),p0,false))
        }
        override fun getItemCount()=data.size
        override fun onBindViewHolder(h: Holder, p: Int) {
            h.binding.apply {
                val item=data[p]
                name.sub=item?.name?:""
                cardNo.sub=item?.cardNo?:""
                email.sub=item?.email?:""
                number.sub=item?.mobileLink?:""
                vehicleNumber.sub=item?.vehicleNo?:""
                date.sub=item?.activteDate?:""
                expiry.sub=item?.renewDate?:""

                edit.setOnClickListener{
                    Intent(this@OwnCardActivity,EditCardActivity::class.java).run {
                        putExtra("data",item)
                        startActivity(this)
                    }
                }
            }
        }
    }

    private fun setCards(data: List<ActivatedCardResponse.Cardinfo?>?) {
        if(data.isNullOrEmpty()){
            binding.apply {
                noData.visibility= View.VISIBLE
                recyclerView.visibility=View.GONE
            }
        }else{
            binding.apply {
                noData.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                val adapter = Adapter(data)
                recyclerView.adapter = adapter
            }
        }
    }
}