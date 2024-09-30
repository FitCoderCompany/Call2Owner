package com.call2owner.ui.cart

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.call2owner.R
import com.call2owner.databinding.FragmentCartBinding
import com.call2owner.databinding.ItemCartBinding
import com.call2owner.databinding.LayoutAddressBinding
import com.call2owner.databinding.LayoutGetAddressBinding
import com.call2owner.model.CartEditRequest
import com.call2owner.model.CartRequest
import com.call2owner.model.CartResponse
import com.call2owner.model.CommonRequest
import com.call2owner.model.CommonResponse
import com.call2owner.model.PaymentStatusRequest
import com.call2owner.model.PaymentUrlResponse
import com.call2owner.model.ProfileResponse
import com.call2owner.model.ShippingRequest
import com.call2owner.ui.BaseFragment
import com.call2owner.ui.activity.MainActivity
import com.call2owner.utils.MyUtil
import com.call2owner.utils.MyUtil.fitDialog
import com.call2owner.utils.MyUtil.getCommaCurrency
import com.call2owner.utils.MyUtil.isValidEmail
import com.call2owner.utils.MyUtil.log
import com.call2owner.utils.MyUtil.model
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class CartFragment : BaseFragment() {
    private lateinit var binding: FragmentCartBinding
    private val cartID: String by lazy { "getCartID" }
    private val statusCheckID: String by lazy { "statusCheckID" }
    private val removeID: String by lazy { "removeID" }
    private val updateID: String by lazy { "updateID" }
    private val shippingID: String by lazy { "shippingID" }
    private val getProfileID: String by lazy { "getProfileID" }
    private var totalAmountToPaid = 0.0
    private var removedPosition = 0
    var isGoneForPayment = false
    var paymentOrderID = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater)
        getCarts()
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        if (isGoneForPayment) {
            isGoneForPayment = false
            checkForResult()
        }
    }

    private fun checkForResult() {
        val req = PaymentStatusRequest(
            orderID = paymentOrderID,
            uid = userData.id,
            CartID = userData.cartID
        )
        apiManager.makeRequest(
            statusCheckID,
            true,
            "Fetching Cart",
            myApiService.paymentStatusRequest(req),
            this
        )
    }


    private fun getCarts() {
        val req = CartRequest(action = "checkoutList", cartID = userData.cartID, uid = userData.id)
        apiManager.makeRequest(cartID, true, "Fetching Cart", myApiService.cartList(req), this)
    }

    private fun emptyData() {
        binding.apply {
            emptyLayout.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            subTotal.text = getString(R.string.subtotal_, getCommaCurrency(0.0))
            proceed.text = getString(R.string.start_shopping)
            proceed.setOnClickListener {
                (requireActivity() as MainActivity).navController?.navigate(R.id.navigation_home)
            }
        }
    }

    fun updateData() {
        binding.apply {
            val data = (recyclerView.adapter as CartAdapter).getAllData()
            if (data.isEmpty()) {
                emptyData()
            } else {
                updateValues(data)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateValues(data: List<CartResponse.Data?>) {
        var totalAmount = 0.0
        data.forEach {
            totalAmount += (it?.totalAmount?.toDouble() ?: 0.0)
        }
        binding.apply {
            itemCount.text = data.size.toString()
            proceed.text = getString(R.string.proceed_to_buy_items_, data.size.toString())
            subTotal.text = getString(R.string.subtotal_, MyUtil.getCommaCurrency(totalAmount))

            proceed.setOnClickListener {
                if (userData.isProfileFetched) {
                    val req = ShippingRequest(
                        action = "shipping",
                        address = userData.address1,
                        cartID = userData.cartID,
                        city = userData.city,
                        country = userData.country,
                        email = userData.email,
                        fname = userData.fname,
                        lname = userData.lname,
                        mobile = userData.mobile,
                        pincode = userData.pincode,
                        state = userData.state,
                        totalAmount = totalAmount.toString(),
                        uid = userData.id
                    )
                    getAddress(req)
                } else {
                    totalAmountToPaid = totalAmount
                    getProfile()
                }

            }

        }
    }

    private fun getProfile() {
        val req = CommonRequest(id = userData.id, action = "GetProfile")
        apiManager.makeRequest(
            getProfileID,
            true,
            "Getting Profile Info",
            myApiService.login(req),
            this
        )
    }

    private fun getAddress(req: ShippingRequest) {
        val addressSheet = BottomSheetDialog(requireContext(), R.style.FullBottomSheetTheme)
        val aBinding = LayoutGetAddressBinding.inflate(layoutInflater)
        addressSheet.setContentView(aBinding.root)
        aBinding.apply {
            fName.text = req.fname ?: ""
            lName.text = req.lname ?: ""
            number.text = req.mobile ?: ""
            email.text = req.email ?: ""
            address.text = req.address ?: ""
            city.text = req.city ?: ""
            state.text = req.state ?: ""
            country.text = req.country ?: ""
            pinCode.text = req.pincode ?: ""

            fName.requestFocus()

            placeOrder.setOnClickListener {
                if (MyUtil.validateAll(
                        arrayOf(
                            fName,
                            lName,
                            number,
                            email,
                            address,
                            city,
                            state,
                            country,
                            pinCode
                        )
                    )
                ) {
                    var good = true
                    if (number.text.length < 10) {
                        good = false
                        number.setError("Enter 10 Digit Number")
                    }
                    if (!email.text.isValidEmail()) {
                        good = false
                        email.setError("Enter Valid Email ID")
                    }

                    if (good) {
                        val reqs = ShippingRequest(
                            action = "shipping",
                            address = address.text,
                            cartID = userData.cartID,
                            city = city.text,
                            country = country.text,
                            email = email.text,
                            fname = fName.text,
                            lname = lName.text,
                            mobile = number.text,
                            pincode = pinCode.text,
                            state = state.text,
                            totalAmount = req.totalAmount,
                            uid = userData.id
                        )
                        addressSheet.dismiss()
                        confirmAddressSheet(reqs)
                    }
                }
            }
        }
        addressSheet.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        addressSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        addressSheet.show()
    }

    @SuppressLint("SetTextI18n")
    private fun confirmAddressSheet(req: ShippingRequest) {
        val netAmount = req.totalAmount
        val confirmSheet = BottomSheetDialog(requireContext(), R.style.FullBottomSheetTheme)
        val cBinding = LayoutAddressBinding.inflate(layoutInflater)
        confirmSheet.setContentView(cBinding.root)
        cBinding.apply {
            price.sub = getCommaCurrency(req.totalAmount)
            val deliveryCharge = 0.00 // to be changed
            delivery.sub = getCommaCurrency(deliveryCharge)

            req.totalAmount = ((req.totalAmount?.toDouble() ?: 0.0) + deliveryCharge).toString()

            total.sub = getCommaCurrency(req.totalAmount)
            profileAddress.text =
                "${req.address}, ${req.city}, ${req.state}, ${req.country}-${req.pincode}"

            changeAddress.setOnClickListener {
                req.totalAmount = netAmount
                getAddress(req)
                confirmSheet.dismiss()
            }

            startShipping.setOnClickListener {
                confirmSheet.dismiss()
                startShipping(req)
            }
        }
        confirmSheet.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        confirmSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        confirmSheet.show()
    }

    private fun startShipping(req: ShippingRequest) {
        binding.apply {
            apiManager.makeRequest(
                shippingID,
                true,
                "Booking Card",
                myApiService.shipping(req),
                this@CartFragment
            )
        }
    }

    private fun setCartData(data: ArrayList<CartResponse.Data?>?) {
        binding.apply {
            if (data.isNullOrEmpty()) {
                emptyData()
            } else {
                emptyLayout.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                val adapter = CartAdapter(data)
                recyclerView.adapter = adapter
                updateValues(data)
            }
        }
    }

    fun updateDB(item: CartResponse.Data?) {
        val req =
            CartEditRequest("cartEdit", item?.id, item?.pricePerItem, item?.qty, item?.totalAmount)
        apiManager.makeRequest(updateID, false, "", myApiService.editCart(req), this)
    }

    inner class CartAdapter(val data: ArrayList<CartResponse.Data?>) :
        RecyclerView.Adapter<CartAdapter.Holder>() {

        inner class Holder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
            return Holder(ItemCartBinding.inflate(LayoutInflater.from(p0.context), p0, false))
        }

        override fun getItemCount() = data.size

        override fun onBindViewHolder(h: Holder, p: Int) {
            h.binding.apply {
                val item = data[p]
                item?.let {
                    name.text = it.productName
                    price.text =
                        getString(R.string.price_, MyUtil.getCommaCurrency(it.pricePerItem))
                    amount.text =
                        getString(R.string.total_, MyUtil.getCommaCurrency(it.totalAmount))
                    quantity.apply {
                        count.text = it.qty
                        plus.setOnClickListener {
                            data[p]?.qty = ((data[p]?.qty?.toInt() ?: 0) + 1).toString()
                            data[p]?.totalAmount = ((data[p]?.qty?.toDouble()
                                ?: 0.0) * (data[p]?.pricePerItem?.toDouble() ?: 0.0)).toString()
                            notifyItemChanged(p)
                            updateData()
                            updateDB(data[p])
                        }
                        minus.setOnClickListener {
                            if (data[p]?.qty?.toInt() != 1) {
                                data[p]?.qty = ((data[p]?.qty?.toInt() ?: 2) - 1).toString()
                                data[p]?.totalAmount = ((data[p]?.qty?.toDouble()
                                    ?: 0.0) * (data[p]?.pricePerItem?.toDouble() ?: 0.0)).toString()
                                notifyItemChanged(p)
                                updateData()
                                updateDB(data[p])
                            } else if (data[p]?.qty?.toInt() == 1) {
                                deleteItem(item, p)
                            }
                        }
                    }
                    remove.setOnClickListener {
                        deleteItem(item, p)
                    }
                }
            }
        }


        fun getAllData(): List<CartResponse.Data?> {
            return data
        }
    }

    private fun deleteItem(it: CartResponse.Data?, position: Int) {
        fitDialog(
            requireContext(),
            requireActivity(),
            "Delete!",
            "Are you sure you want to remove this item?",
            "Delete",
            "",
            true,
            R.raw.delete,
            true
        ) {
            val req = CommonRequest(action = "remove", id = it?.id)
            removedPosition = position
            apiManager.makeRequest(
                removeID,
                true,
                "Deleting Cart",
                myApiService.deleteCart(req),
                this
            )
        }

    }

    override fun onResult(type: String, success: Boolean, response: String) {
        when (type) {
            statusCheckID -> {
                try {
                    val resp = response.model(CommonResponse::class.java)
                    if (success) {
                        if(resp?.statusCode==422){
                            fitDialog(
                                requireContext(),
                                requireActivity(),
                                "Failed",
                                "Transaction Failed",
                                "Okay",
                                "",
                                false,
                                R.raw.fail_result,
                                true
                            ) {
                                (requireActivity() as MainActivity).navController?.navigate(R.id.navigation_home)
                            }
                        }else{
                            fitDialog(
                                requireContext(),
                                requireActivity(),
                                "Successful",
                                "Order Placed Successfully",
                                "Continue Shopping",
                                "",
                                false,
                                R.raw.success,
                                true
                            ) {
                                (requireActivity() as MainActivity).navController?.navigate(R.id.navigation_home)
                            }
                        }

                    } else {
                        showErrorSnackBar(resp?.message)
                    }
                } catch (e: Exception) {
                    e.log()

                }
            }

            cartID -> {
                try {
                    val resp = response.model(CartResponse::class.java)
                    if (success) {
                        setCartData(resp?.data)
                    } else {
                        emptyData()
//                        showErrorSnackBar(resp?.message)
                    }
                } catch (e: Exception) {
                    emptyData()
                    e.log()
//                    showErrorSnackBar("Bad Format json: ${e.message}")
                }
            }

            shippingID -> {
                try {
                    val resp = response.model(PaymentUrlResponse::class.java)
                    if (success) {
                        startPaymentScreen(resp)
//                        fitDialog(requireContext(),requireActivity(),"Successful","Order Placed Successfully","Continue Shopping","",false,R.raw.success,true){
//                            (requireActivity() as MainActivity).navController?.navigate(R.id.navigation_home)
//                        }
                    } else {
                        showErrorSnackBar(resp?.message)
                    }
                } catch (e: Exception) {
                    e.log()
                    showErrorSnackBar("Bad Format json: ${e.message}")
                }
            }

            removeID -> {
                try {
                    val resp = response.model(CommonResponse::class.java)
                    if (success) {
                        (binding.recyclerView.adapter as CartAdapter).data.removeAt(removedPosition)
                        (binding.recyclerView.adapter as CartAdapter).notifyItemRemoved(
                            removedPosition
                        )
                        updateData()
                    } else {
                        showErrorSnackBar(resp?.message)
                    }
                } catch (e: Exception) {
                    e.log()
                    showErrorSnackBar("Bad Format json: ${e.message}")
                }
            }

            getProfileID -> {
                try {
                    val resp = response.model(ProfileResponse::class.java)
                    if (success) {
                        resp?.data?.apply {
                            userData.isProfileFetched = true
                            userData.fname = fname ?: ""
                            userData.lname = lname ?: ""
                            userData.email = email ?: ""
                            userData.address1 = address ?: ""
                            userData.pincode = pincode ?: ""
                            userData.city = city ?: ""
                            userData.state = state ?: ""
                            userData.country = country ?: ""


                            val req = ShippingRequest(
                                action = "shipping",
                                address = userData.address1,
                                cartID = userData.cartID,
                                city = userData.city,
                                country = userData.country,
                                email = userData.email,
                                fname = userData.fname,
                                lname = userData.lname,
                                mobile = userData.mobile,
                                pincode = userData.pincode,
                                state = userData.state,
                                totalAmount = totalAmountToPaid.toString(),
                                uid = userData.id
                            )
                            getAddress(req)
                        }
                    } else {
                        showErrorSnackBar("${resp?.statusCode}")
                    }
                } catch (e: Exception) {
                    e.log()
                    showErrorSnackBar("Bad Json Format ${e.message}")
                }
            }
        }
    }


    private fun startPaymentScreen(upiUri: PaymentUrlResponse?) {
        try {
            paymentOrderID = upiUri?.orderID ?: ""
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(upiUri?.upiData?.trim() ?: "")
            startActivity(intent)
            isGoneForPayment = true
        } catch (e: Exception) {
            e.log()
            showErrorSnackBar("Did not Found any UPI app in your phone")
        }
    }

}