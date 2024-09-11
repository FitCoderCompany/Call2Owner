package com.call2owner.network.apis
import com.call2owner.model.ActivateCardRequest
import com.call2owner.model.AddCartRequest
import com.call2owner.model.CardInfoRequest
import com.call2owner.model.CartEditRequest
import com.call2owner.model.CartRequest
import com.call2owner.model.CommonRequest
import com.call2owner.model.EditCardRequest
import com.call2owner.model.ShippingRequest
import com.call2owner.model.UpdateProfileRequest
import com.call2owner.network.Constant
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MyApi {

    @POST(Constant.auth)
    fun getToken( ): Call<ResponseBody>


    @POST(Constant.banner)
    fun banner(@Body req: CommonRequest ): Call<ResponseBody>

    @POST(Constant.login)
    fun login(@Body req: CommonRequest): Call<ResponseBody>


    @POST(Constant.login)
    fun orderHistory(@Body req: CommonRequest): Call<ResponseBody>

    @POST(Constant.login)
    fun updateProfile(@Body req: UpdateProfileRequest): Call<ResponseBody>

    @POST(Constant.product)
    fun product(@Body req: CommonRequest): Call<ResponseBody>

    @POST(Constant.checkOut)
    fun cartList(@Body req: CartRequest): Call<ResponseBody>

    @POST(Constant.checkOut)
    fun deleteCart(@Body req: CommonRequest): Call<ResponseBody>

    @POST(Constant.checkOut)
    fun editCart(@Body req: CartEditRequest): Call<ResponseBody>

    @POST(Constant.checkOut)
    fun addCart(@Body req: AddCartRequest): Call<ResponseBody>

    @POST(Constant.checkOut)
    fun shipping(@Body req: ShippingRequest): Call<ResponseBody>

    @POST(Constant.cardApi)
    fun cardApi(@Body req: CardInfoRequest): Call<ResponseBody>

    @POST(Constant.cardApi)
    fun activatedCard(@Body req: CommonRequest): Call<ResponseBody>

    @POST(Constant.cardApi)
    fun editCard(@Body req: EditCardRequest): Call<ResponseBody>

    @POST(Constant.cardApi)
    fun activateCard(@Body req: ActivateCardRequest): Call<ResponseBody>


}