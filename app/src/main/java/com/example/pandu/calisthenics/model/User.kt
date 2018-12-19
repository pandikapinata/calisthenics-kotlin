package com.example.pandu.calisthenics.model


data class User(
    val id: Long?,
    val idUser: String? = null,
    val name: String? = null,
    val email: String? = null,
    val fcm_token: String? = null,
    val weight: String? = null,
    val height: String? = null,
    val photo_profile: String? = null
){
    companion object {
        const val TABLE_USER : String = "TABLE_USER"
        const val ID : String = "ID_"
        const val USER_ID : String = "USER_ID"
        const val NAME_USER : String = "NAME_USER"
        const val EMAIL_USER : String = "EMAIL_USER"
        const val FCM_TOKEN_USER : String = "FCM_TOKEN_USER"
        const val WEIGHT_USER : String = "WEIGHT_USER"
        const val HEIGHT_USER : String = "HEIGHT_USER"
        const val PHOTO_PROFILE : String = "PHOTO_PROFILE"
    }


}