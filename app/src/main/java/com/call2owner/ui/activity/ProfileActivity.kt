package com.call2owner.ui.activity

import android.os.Bundle
import com.call2owner.R
import com.call2owner.custom.CustomTextView
import com.call2owner.databinding.ActivityProfileBinding
import com.call2owner.model.CommonResponse
import com.call2owner.model.UpdateProfileRequest
import com.call2owner.ui.BaseActivity
import com.call2owner.utils.MyUtil.fitDialog
import com.call2owner.utils.MyUtil.isValidEmail
import com.call2owner.utils.MyUtil.log
import com.call2owner.utils.MyUtil.model

class ProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val updateID = "updateID"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setProfileData()
        setContentView(binding.root)
    }

    private fun setProfileData() {
        binding.apply {
            fName.text = userData.fname
            lName.text = userData.lname
            number.text = userData.mobile
            email.text = userData.email
            address.text = userData.address1 + " " + userData.address2
            city.text = userData.city
            pinCode.text = userData.pincode
            state.text = userData.state
            country.text = userData.country.ifEmpty { "India" }

            update.setOnClickListener {
                if (validateAll(
                        arrayOf(
                            fName,
                            lName,
                            number,
                            email,
                            address,
                            city,
                            pinCode,
                            state,
                            country
                        )
                    )
                ) {
                    if (email.text.isValidEmail()) {
                        val req = UpdateProfileRequest(
                            id = userData.id,
                            action = "profileUpdate",
                            fname = fName.text,
                            lname = lName.text,
                            email = email.text,
                            city = city.text,
                            address = address.text,
                            state = state.text,
                            country = country.text,
                            pincode = pinCode.text
                        )
                        apiManager.makeRequest(
                            updateID,
                            true,
                            "Saving Profile",
                            myApiService.updateProfile(req),
                            this@ProfileActivity
                        )
                    } else {
                        email.setError("Enter Valid Email ID")
                    }
                }


            }
        }

    }

    private fun validateAll(data: Array<CustomTextView>): Boolean {
        var good = true
        data.forEach {
            if (it.text.isEmpty()) {
                good = false
                it.setError()
            }
        }

        return good
    }

    private fun updateData() {
        binding.apply {
            userData.fname = fName.text
            userData.lname = lName.text
            userData.email = email.text
            userData.address1 = address.text
            userData.pincode = pinCode.text
            userData.state = state.text
            userData.country = country.text
        }
    }

    override fun onResult(type: String, success: Boolean, response: String) {
        when (type) {
            updateID -> {
                try {
                    val resp = response.model(CommonResponse::class.java)
                    if (success) {
                        updateData()
                        fitDialog(context, activity, "", "Profile Updated Successfully", "Ok", "", true, R.raw.success, true) { }
                    } else {
                        showErrorSnackBar(resp?.message)
                    }
                } catch (e: Exception) {
                    e.log()
                    showErrorSnackBar("Bad Json Format ${e.message}")
                }
            }
        }
    }


}