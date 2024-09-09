package com.call2owner.ui.history

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.call2owner.R
import com.call2owner.databinding.FragmentHistoryBinding
import com.call2owner.databinding.ItemCartBinding
import com.call2owner.databinding.ItemOrderHistoryBinding
import com.call2owner.model.CartResponse
import com.call2owner.model.CommonRequest
import com.call2owner.model.HistoryResponse
import com.call2owner.ui.BaseFragment
import com.call2owner.ui.activity.ProductDetailsActivity
import com.call2owner.utils.MyUtil
import com.call2owner.utils.MyUtil.log
import com.call2owner.utils.MyUtil.model

class HistoryFragment : BaseFragment() {
    private lateinit var binding:FragmentHistoryBinding
    private val historyID="historyID"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding=FragmentHistoryBinding.inflate(layoutInflater)
        getHistory()
        return binding.root
    }

    private fun getHistory() {
        val req=CommonRequest(uid=userData.id, action = "OrderList")
        apiManager.makeRequest(historyID,true,"Fetching Order List",myApiService.orderHistory(req),this)


    }

    private fun initView(orderList: ArrayList<HistoryResponse.Order?>?) {
        binding.apply {
            if(orderList.isNullOrEmpty()){
                emptyData()
            }else{
                recyclerView.visibility=View.VISIBLE
                noData.visibility=View.GONE
                val adapter=Adapter(orderList)
                recyclerView.adapter=adapter
                search.getEditView().doAfterTextChanged {
                    it?.toString()?.let { text ->
                        adapter.filter(text)

                    }
                }
            }
        }
    }
    private fun emptyData() {
        binding.apply {
            recyclerView.visibility=View.GONE
            noData.visibility=View.VISIBLE
        }
    }

    override fun onResult(type: String, success: Boolean, response: String) {
        when(type){
            historyID->{
                try{
                    val resp=response.model(HistoryResponse::class.java)
                    if(success){
                        initView(resp?.orderList)
                    }else{
                        emptyData()
                        showErrorSnackBar(resp?.message )
                    }

                }catch (e:Exception){
                    e.log()
                    showErrorSnackBar("Bad Format: ${e.message}")
                }
            }
        }
    }


    inner class Adapter(val data: ArrayList<HistoryResponse.Order?>): RecyclerView.Adapter<Adapter.Holder>(){
        var filterData=data.toList()
        inner class Holder(val binding: ItemOrderHistoryBinding): RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
            return Holder(ItemOrderHistoryBinding.inflate(LayoutInflater.from(p0.context),p0,false))
        }

        override fun getItemCount()=filterData.size

        override fun onBindViewHolder(h: Holder, p: Int) {
            h.binding.apply {
                val item=filterData[p]
                item?.let {
                    name.text=it.productName
                    amount.text= getString(R.string.total_, MyUtil.getCommaCurrency(it.totalAmount))
                    date.text= getString(R.string.ordered_on_, it.orderDate)
                    status.text= getString(R.string.status_, it.status)
                    main.setOnClickListener{_->
                        Intent(requireContext(), OrderDetailsActivity::class.java).run{
                            putExtra("data",it)
                            startActivity(this)
                        }
                    }
                }
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        fun filter(text: String) {
            filterData=data.filter {
                it?.productName?.contains(text,true)==true ||
                it?.orderno?.contains(text,true)==true ||
                it?.totalAmount?.contains(text,true)==true ||
                it?.eMail?.contains(text,true)==true ||
                it?.mobile?.contains(text,true)==true
            }
            notifyDataSetChanged()
        }
    }
}