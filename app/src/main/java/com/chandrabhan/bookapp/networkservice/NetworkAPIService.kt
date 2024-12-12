package com.chandrabhan.bookapp.networkservice

import com.google.gson.GsonBuilder
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NetworkAPIService {

    private const val BASE_URL = "https://www.googleapis.com/"
    private val interceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    private val certificatePinner = CertificatePinner.Builder().apply {
        add("upload.video.google.com","sha256/af2b3d0ade21ec86e9998620ca09cba448050f7aea334e6ed0fd32d8e39c33e2")
    }.build()


    private val client = OkHttpClient.Builder().apply {
        certificatePinner
        addInterceptor(interceptor)
    }.build()
    private fun retrofitService(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(client)
            .build()
    }

    val apiService: APIService by lazy {
        retrofitService().create(APIService::class.java)
    }

}