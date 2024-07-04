package com.call2owner.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.call2owner.R
import com.call2owner.databinding.FragmentProfileBinding
import com.call2owner.ui.BaseFragment
import com.call2owner.ui.activity.MainActivity
import com.call2owner.ui.activity.auth.LoginActivity
import com.call2owner.utils.MyUtil

class ProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        checkLogin()
    }

    private fun startLogin(){
        start(LoginActivity::class.java)
    }
    private fun checkLogin() {
        if(userData.userName.isEmpty()){
           val d= MyUtil.fitDialog(
               context = requireContext(),
               activity = requireActivity(),
               title = "",
               msg = "Please login to continue",
               okTxt = "Login",
               cancelTxt = "Cancel",
               cancelable = false,
               icon = null,
               toShow = true,
               okFun = ::startLogin
           )
            d.second.cancel.setOnClickListener{
                start(MainActivity::class.java)
                requireActivity().finishAffinity()
            }
        }else{

            binding.apply {
                fName.text=userData.fname
                lName.text=userData.lname
                number.text=userData.phoneNumber
                email.text=userData.email
                address.text=userData.address1 + " " + userData.address2 + " " + userData.city
                pinCode.text=userData.pincode
                state.text=userData.state
                country.text=userData.country.ifEmpty { "India" }
            }

        }
    }
}