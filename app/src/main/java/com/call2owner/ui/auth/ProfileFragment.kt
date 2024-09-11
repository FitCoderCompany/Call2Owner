package com.call2owner.ui.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.call2owner.R
import com.call2owner.databinding.FragmentProfileBinding
import com.call2owner.model.CommonRequest
import com.call2owner.model.ProfileResponse
import com.call2owner.ui.BaseFragment
import com.call2owner.ui.activity.MainActivity
import com.call2owner.ui.activity.OwnCardActivity
import com.call2owner.ui.activity.ProfileActivity
import com.call2owner.ui.activity.auth.LoginActivity
import com.call2owner.ui.activity.ContactActivity
import com.call2owner.utils.MyUtil.explicitWeb
import com.call2owner.utils.MyUtil.fitDialog
import com.call2owner.utils.MyUtil.log
import com.call2owner.utils.MyUtil.model
import com.call2owner.utils.MyUtil.openImplicitWeb

class ProfileFragment : BaseFragment() {
    private lateinit var binding: FragmentProfileBinding
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
           d.second.cancel.setOnClickListener {
               d.first.dismiss()
               (requireActivity() as MainActivity).navController?.navigate(R.id.navigation_home)
           }
        }else{
            val req=CommonRequest(id=userData.id,action="GetProfile")
            apiManager.makeRequest(getProfileID,true,"Getting Profile Info",myApiService.login(req),this)
            setProfileData()

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setProfileData() {
        binding.apply {

            if(userData.fname.isEmpty()){
                val d= fitDialog(requireContext(), requireActivity(), "", "Please Complete your profile", "Ok", "Cancel", false, R.raw.profile, true){
                    start(ProfileActivity::class.java)
                }
                d.second.cancel.setOnClickListener{
                    d.first.dismiss()
                    ( requireActivity() as MainActivity).navController?.navigate(R.id.navigation_home)
                }
            }


            name.text="${userData.fname} ${userData.lname }".trim()
            number.text=userData.mobile
            email.text=userData.email
            address.text="${userData.address1}, ${userData.city}, ${userData.state}, ${userData.country}-${userData.pincode}"

            version.text="Version ${requireContext().packageManager.getPackageInfo(requireContext().packageName, 0).versionName} | Made in India"

            activatedCard.setOnClickListener{
                start(OwnCardActivity::class.java)
            }

            edit.setOnClickListener{
                start(ProfileActivity::class.java)
            }
            privacy.setOnClickListener{
                requireContext().openImplicitWeb("https://call2owner.com/privacy-policy")
            }
            terms.setOnClickListener{
                requireContext().openImplicitWeb("https://call2owner.com/terms-and-condition")
            }

            faq.setOnClickListener{
                requireContext().openImplicitWeb("https://call2owner.com/frequently-asked-questions")
            }

            vehiclePolicy.setOnClickListener{
                requireContext().openImplicitWeb("https://call2owner.com/vehicle-tag-privacy-policy")
            }
            returnPolicy.setOnClickListener{
                requireContext().openImplicitWeb("https://call2owner.com/return-policy")
            }

            use.setOnClickListener{
                requireContext().openImplicitWeb("https://call2owner.com/how-to-use")
            }
            about.setOnClickListener{
                requireContext().openImplicitWeb("https://call2owner.com/about-us")
            }
            contact.setOnClickListener{
                start(ContactActivity::class.java)
            }
            instagram.setOnClickListener{
                requireContext().explicitWeb("https://www.instagram.com/call2owner/")
            }
            linkedin.setOnClickListener{
                requireContext().explicitWeb("https://www.linkedin.com/company/call2owner/")
            }
            facebook.setOnClickListener{
                requireContext().explicitWeb("https://www.facebook.com/call2owner")
            }
            twitter.setOnClickListener{
                requireContext().explicitWeb("https://x.com/Call2Owner")
            }
            youtube.setOnClickListener{
                requireContext().explicitWeb("https://www.youtube.com/@call2owner")
            }
        }
    }

    override fun onResult(type: String, success: Boolean, response: String) {
        when(type){
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