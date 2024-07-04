package com.call2owner.di

import com.call2owner.network.apis.MyApi
import com.call2owner.network.Constant
import com.call2owner.network.TokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Provides
    fun provideOkHttpClient(  interceptor: TokenInterceptor): OkHttpClient {
        return OkHttpClient
            .Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }


    @Provides
    @Singleton
    fun provideApiService(okhttpClient: OkHttpClient): MyApi {
        return Builder()
            .baseUrl(Constant.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttpClient)
            .build().create(MyApi::class.java)

    }


}


