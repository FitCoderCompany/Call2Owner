package com.call2owner.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.call2owner.R
import com.call2owner.databinding.FragmentProfileBinding
import com.call2owner.model.CommonRequest
import com.call2owner.model.CommonResponse
import com.call2owner.model.ProfileResponse
import com.call2owner.model.UpdateProfileRequest
import com.call2owner.ui.BaseFragment
import com.call2owner.ui.activity.MainActivity
import com.call2owner.ui.activity.auth.LoginActivity
import com.call2owner.utils.MyUtil
import com.call2owner.utils.MyUtil.fitDialog
import com.call2owner.utils.MyUtil.log
import com.call2owner.utils.MyUtil.model

class ProfileFragment : BaseFragment() {
    private lateinit var binding: FragmentProfileBinding
    private val updateID="updateID"
    private val getProfileID="getProfileID"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        checkLogin()
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
            val req=CommonRequest(id=userData.id,action="GetProfile")

            apiManager.makeRequest(getProfileID,true,"Getting Profile Info",myApiService.login(req),this)
            setProfileData()

        }
    }

    private fun setProfileData() {
        binding.apply {
            fName.text=userData.fname
            lName.text=userData.lname
            number.text=userData.mobile
            email.text=userData.email
            address.text=userData.address1 + " " + userData.address2
            city.text=userData.city
            pinCode.text=userData.pincode
            state.text=userData.state
            country.text=userData.country.ifEmpty { "India" }

            update.setOnClickListener{
                val req= UpdateProfileRequest(
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
                apiManager.makeRequest(updateID,true,"Saving Profile",myApiService.updateProfile(req),this@ProfileFragment)
            }
        }

    }

    private fun updateData() {
        binding.apply {
            userData.fname = fName.text
            userData.lname = lName.text
            userData.email =  email.text
            userData.address1 = address.text
            userData.pincode = pinCode.text
            userData.state = state.text
            userData.country = country.text
        }
    }

    override fun onResult(type: String, success: Boolean, response: String) {
        when(type){
            updateID-> {
                try{
                    val resp=response.model(CommonResponse::class.java)
                    if (success) {
                        updateData()
                        
                        fitDialog(requireContext(),requireActivity(),"","Profile Updated Successfully","Ok","",true,R.raw.success,true){ }
                    } else {
                        showErrorSnackBar(resp?.message)
                    }
                }catch (e:Exception){
                    e.log()
                    showErrorSnackBar("Bad Json Format ${e.message}")
                }
            }

            getProfileID-> {
                try{
                    val resp=response.model(ProfileResponse::class.java)
                    if (success) {
                        resp?.data?.apply {
                            userData.fname = fname?:""
                            userData.lname = lname?:""
                            userData.email =  email?:""
                            userData.address1 = address?:""
                            userData.pincode = pincode?:""
                            userData.city = city?:"" 
                            userData.state = state?:""
                            userData.country = country?:""
                            setProfileData()
                        }
                    } else {
                        showErrorSnackBar("${resp?.statusCode}")
                    }
                }catch (e:Exception){
                    e.log()
                    showErrorSnackBar("Bad Json Format ${e.message}")
                }
            }
        }
    }

   
}