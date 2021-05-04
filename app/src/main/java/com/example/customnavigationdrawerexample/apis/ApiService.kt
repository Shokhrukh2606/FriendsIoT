package com.example.customnavigationdrawerexample.apis

import com.example.customnavigationdrawerexample.constants.Urls
import com.example.customnavigationdrawerexample.data.model.LoginRequest
import com.example.customnavigationdrawerexample.data.model.LoginResponse
import com.example.customnavigationdrawerexample.data.model.MapFields
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST(Urls.LOGIN_URL)
    fun login(@Body request: LoginRequest): Call<LoginResponse>
    @GET(Urls.MAP_FIELDS_URL)
    fun getFields(): Call<MapFields>
}