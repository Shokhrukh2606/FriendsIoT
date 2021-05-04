package com.example.customnavigationdrawerexample.data.model

data class LoginResponse(
    val district_id: Int,
    val name: String,
    val permissions: List<String>,
    val point_id: Int,
    val role: Int,
    val token: String
)