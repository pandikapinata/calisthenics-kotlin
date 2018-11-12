package com.example.pandu.calisthenics.menu.profile

import com.example.pandu.calisthenics.model.User


interface ProfileView {
    fun showLoading()
    fun hideLoading()
    fun getProfileUser(user: User)
    fun onError()
    fun onFailure(t: Throwable)
}