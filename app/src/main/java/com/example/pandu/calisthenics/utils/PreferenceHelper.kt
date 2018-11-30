package com.example.pandu.calisthenics.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.pandu.calisthenics.model.AuthResponse


class PreferenceHelper (context: Context?){

    private val TOKEN = "token"
    private val LOGIN = "login"

    private val NAME = "name"
    private val EMAIL = "email"
    private val FCM_TOKEN = "fcm_token"
    private val WEIGHT = "weight"
    private val HEIGHT = "height"
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

//    save device token
    internal var deviceToken : String? = preferences.getString(TOKEN, "")
    set(token) =  preferences.edit().putString(TOKEN, token).apply()


    fun setLogin(login: Boolean) {
        preferences.edit().putBoolean(LOGIN, login).apply()
    }

    fun setName(name: String?) {
        preferences.edit().putString(NAME, name).apply()
    }

    fun setEmail(email: String?) {
        preferences.edit().putString(EMAIL, email).apply()
    }


    fun setFCM(fcm: String?) {
        preferences.edit().putString(FCM_TOKEN, fcm).apply()
    }

    fun setWeight(weight: String?) {
        preferences.edit().putString(WEIGHT, weight).apply()
    }

    fun setHeight(height: String?) {
        preferences.edit().putString(HEIGHT, height).apply()
    }

    internal var getName : String? = preferences.getString(NAME, "")
    internal var getEmail : String? = preferences.getString(EMAIL, "")
    internal var getFCM : String? = preferences.getString(FCM_TOKEN, "")
    internal var getWeight : String? = preferences.getString(WEIGHT, "")
    internal var getHeight : String? = preferences.getString(HEIGHT, "")


    fun setUserLogin(user: AuthResponse){
        setLogin(true)
        preferences.edit().putString(TOKEN, user.access_token).apply()

    }

    fun logout(){
        preferences.edit().clear().apply()
    }

}