package com.example.customnavigationdrawerexample.apis

import android.content.Context
import com.example.customnavigationdrawerexample.constants.Urls.API_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Retrofit instance class
 */
class ApiClient {
    private lateinit var apiService: ApiService

    fun getApiService(context:Context): ApiService {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY;

        if (!::apiService.isInitialized) {
            val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            httpClient.addInterceptor(logging);
            httpClient.addInterceptor(AuthInterceptor(context))
            val retrofit = Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create()).client(httpClient.build())
                .build()

            apiService = retrofit.create(ApiService::class.java)
        }

        return apiService
    }

}