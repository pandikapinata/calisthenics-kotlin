package com.example.pandu.calisthenics.api

import android.content.Context
import android.util.Log
import com.example.pandu.calisthenics.menu.profile.ProfileFragment
import com.example.pandu.calisthenics.utils.PreferenceHelper
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


class APIClient {
    companion object {
        fun getService(context: Context?): ApiInterface {
            val preferencesHelper = PreferenceHelper(context)
            val baseUrl = "http://192.168.43.74:8000/api/"
            val apiToken = preferencesHelper.deviceToken
            //check apiToken already in there
            Log.i("apiToken", "$apiToken")
            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request: Request = chain
                        .request()
                        .newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer $apiToken")
                        .build()
                    chain.proceed(request)
                }.build()


            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiInterface::class.java)
        }

    }
}