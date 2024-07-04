package com.call2owner.network.apis


import com.call2owner.model.CommonRequest
import com.call2owner.model.CommonResponse
import com.call2owner.network.Constant
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MyApi {

//    @GET(Constant.ipAddress)
//    fun getIPAddress(): Call<ResponseBody>


//    @Multipart
//    @POST(Constant.documentUpload)
//    fun documentUpload(
//        @Query("filetype") filetype: String,
//        @Query("oldfile") oldfile: String,
//        @Query("usercode") usercode: String,
//        @Query("bankaccount") bankaccount: String,
//        @Part file: MultipartBody.Part
//    ): Call<ResponseBody>

    @POST(Constant.auth)
    fun getToken( ): Call<ResponseBody>


    @POST(Constant.banner)
    fun banner(@Body req: CommonRequest ): Call<ResponseBody>

    @POST(Constant.login)
    fun login(@Body req: CommonRequest): Call<ResponseBody>

    @POST(Constant.product)
    fun product(@Body req: CommonRequest): Call<ResponseBody>





//    @FormUrlEncoded
//    @POST(Constant.addWallet)
//    fun addWallet(
//        @Field("orderId") orderId: String,
//        @Field("amount") amount: Double,
//        @Field("responseCode") responseCode: Int
//    ): Call<ResponseBody>

}