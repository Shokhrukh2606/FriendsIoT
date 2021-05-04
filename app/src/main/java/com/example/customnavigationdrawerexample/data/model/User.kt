package com.example.customnavigationdrawerexample.data.model

data class User(
    val name: String,
    val permissions: List<String>,
    val point_id: Int,
    val role: Int,
    val token: String
)