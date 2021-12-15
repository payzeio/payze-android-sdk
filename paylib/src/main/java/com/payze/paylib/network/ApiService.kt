package com.payze.paylib.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {

    companion object {
        private var instance: ApiService? = null

        private const val BASE_URL = "https://paygate.payze.io/"

        fun initialize() {
            if (instance == null) {
                instance = create()
            }
        }

        fun get(): ApiService {
            return instance!!
        }

        private fun create(): ApiService {

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            val okHttpClient = OkHttpClient.Builder()
//                .addInterceptor(AuthInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

            val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
//                .addCallAdapterFactory()
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }


    @FormUrlEncoded
    @POST("mobile/cardInfo")
    fun sendPaymentData(
        @Field("number") number: String,
        @Field("cardHolder") cardHolder: String,
        @Field("expirationDate") expirationDate: String,
        @Field("securityNumber") securityNumber: String,
        @Field("transactionId") transactionId: String,
        @Field("billingAddress") billingAddress: String,
    ): Call<PaymentResponse>

    @GET("mobile/transactionStatus")
    fun getTransactionStatus(@Query("transactionId") transactionID: String): Call<TransactionStatusResponse>
}