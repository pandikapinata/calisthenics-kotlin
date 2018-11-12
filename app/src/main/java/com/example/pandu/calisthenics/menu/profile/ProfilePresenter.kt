package com.example.pandu.calisthenics.menu.profile

import com.example.pandu.calisthenics.api.ApiInterface
import com.example.pandu.calisthenics.model.AuthResponse
import com.example.pandu.calisthenics.model.User
import retrofit2.Callback
import retrofit2.Response

class ProfilePresenter(private val view: ProfileView, private val service: ApiInterface) {
    fun showProfile() {
        view.showLoading()
        service.userProfile()
                .enqueue(object : Callback<User> {
                    override fun onResponse(call: retrofit2.Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            view.getProfileUser(response.body()!!)
                        } else {
                            view.onError()
                        }
                        view.hideLoading()
                    }

                    override fun onFailure(call: retrofit2.Call<User>, t: Throwable) {
                        view.onFailure(t)
                        view.hideLoading()
                    }
                })
    }
}