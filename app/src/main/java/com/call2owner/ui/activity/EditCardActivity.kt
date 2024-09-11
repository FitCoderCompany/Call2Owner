package com.call2owner.ui.activity

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.call2owner.R
import com.call2owner.databinding.ActivityEditCardBinding
import com.call2owner.model.ActivatedCardResponse
import com.call2owner.model.EditCardRequest
import com.call2owner.ui.BaseActivity
import com.call2owner.utils.MyUtil
import com.call2owner.utils.MyUtil.isValidEmail

class EditCardActivity : BaseActivity() {
    private lateinit var binding: ActivityEditCardBinding
    private val editCardID="editCardID"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intiView()
    }

    private fun intiView() {
        binding.apply {
            val data= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra("data", ActivatedCardResponse.Cardinfo::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getSerializableExtra("data") as ActivatedCardResponse.Cardinfo
            }

            name.text=data?.name?:""
            cardNo.text=data?.cardNo?:""
            email.text=data?.email?:""
            number.text=data?.mobileLink?:""
            vehicleNumber.text=data?.vehicleNo?:""
            date.text=data?.activteDate?:""
            expiry.text=data?.renewDate?:""



            submit.setOnClickListener{
                var good=true

                if(email.isEmpty()){
                    good=false
                    email.setError()
                }else if(!email.text.isValidEmail()){
                    good=false
                    email.setError()
                }

                if(number.isEmpty()){
                    good=false
                    number.setError()
                }



                if(good){
                    val req=EditCardRequest(
                        action = "UpdateCardInfo",
                        cardno = data?.cardNo,
                        email = email.text,
                        phone = number.text
                    )

                    apiManager.makeRequest(editCardID,true,"Updating Card",myApiService.editCard(req),this@EditCardActivity)
                }
            }
        }
    }

    override fun onResult(type: String, success: Boolean, response: String) {
        when(type) {
            editCardID -> {
                if (success) {
                    MyUtil.fitDialog(
                        context,
                        activity,
                        "",
                        "Update Successful",
                        "",
                        "",
                        false,
                        R.raw.success,
                        true
                    ) {
                        finish()
                    }
                } else {
                    showErrorSnackBar(response)
                }

            }
        }
    }
}