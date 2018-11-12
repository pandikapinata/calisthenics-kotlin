package com.example.pandu.calisthenics.api

import android.content.Context
import com.example.pandu.calisthenics.menu.profile.ProfileFragment
import com.example.pandu.calisthenics.utils.PreferenceHelper
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.Request


class APIClient {
    companion object {
        fun getService(context: Context?): ApiInterface {
            val preferencesHelper = PreferenceHelper(context)
            val baseUrl = "https://book-api-progmob.herokuapp.com/api/"
            val apiToken = preferencesHelper.deviceToken
            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request: Request
                    if (preferencesHelper.getLogin()) {
                        request = chain
                            .request()
                            .newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", "Bearer $apiToken")
                            .build()
                    } else {
                        request = chain
                            .request()
                            .newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .build()
                    }
                    chain.proceed(request)
                }.build()


            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiInterface::class.java)
        }

    }
}