package com.example.pandu.calisthenics.auth

import com.example.pandu.calisthenics.model.AuthResponse
import com.example.pandu.calisthenics.model.BasicResponse
import com.example.pandu.calisthenics.model.User


interface AuthView {
    fun showLoading()
    fun hideLoading()
    fun onSuccess(userAuth: AuthResponse)
    fun onError()
    fun onFailure(t: Throwable)
}