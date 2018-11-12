package com.example.pandu.calisthenics.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.pandu.calisthenics.model.AuthResponse


class PreferenceHelper (context: Context?){

    private val PREFERENCES_NAME = "shared_preferences"
    private val TOKEN = "token"
    private val NAME = "name"
    private val LOGIN = "login"
    private val MODE = Context.MODE_PRIVATE
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

//    save device token
internal var deviceToken : String? = preferences.getString(TOKEN, "")
    set(token) =  preferences.edit().putString(TOKEN, token).apply()


    fun setLogin(login: Boolean) {
        preferences.edit().putBoolean(LOGIN, login).apply()
    }

    fun getLogin(): Boolean {
        return preferences.getBoolean(LOGIN, false)
    }

    fun setName(name: String?) {
        preferences.edit().putString(NAME, name).apply()
    }

    fun getName(): String? {
        return preferences.getString(NAME, "")
    }

    fun setUserLogin(user: AuthResponse){
        setLogin(true)
        preferences.edit().putString(TOKEN, user.access_token).apply()

    }

    fun logout(){
        preferences.edit().clear().apply()
    }

}