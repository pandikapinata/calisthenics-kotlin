package com.example.pandu.calisthenics.model


data class AuthResponse(
    var access_token: String? = null,
    var status: Boolean? = null,
    var token_type: String? = null,
    var message: String? = null,
    var errors: String? = null
)