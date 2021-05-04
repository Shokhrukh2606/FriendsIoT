package com.example.customnavigationdrawerexample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.customnavigationdrawerexample.apis.ApiClient
import com.example.customnavigationdrawerexample.data.model.LoginRequest
import com.example.customnavigationdrawerexample.data.model.LoginResponse
import com.example.customnavigationdrawerexample.manager.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        var userNameInput   = findViewById<EditText>(R.id.username_input);
        var userPasswordInput   = findViewById<EditText>(R.id.password);
        var loginButton   = findViewById<Button>(R.id.login);

        loginButton.setOnClickListener {
            apiClient.getApiService(this).login(
                LoginRequest(
                    username = userNameInput.text.toString(),
                    password = userPasswordInput.text.toString()
                )
            )
                .enqueue(
                    object : Callback<LoginResponse> {
                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            // Error logging in
                            Log.v("BackError", t.toString())
                        }

                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>,
                        ) {
                            val loginResponse = response.body()
                            if (loginResponse?.token != null) {
                                Log.v("Success", loginResponse.toString())
                                sessionManager.saveAuthToken(loginResponse.token)
                                val text = "You are logged in"
                                val duration = Toast.LENGTH_SHORT
                                val toast = Toast.makeText(this@LoginActivity, text, duration)
                                toast.show()
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                Log.v("MyError", loginResponse.toString())
                                val text = "Username or password incorrect. Try again!"
                                val duration = Toast.LENGTH_SHORT
                                val toast = Toast.makeText(this@LoginActivity, text, duration)
                                toast.show()
                            }
                        }
                    },
                )
        }


    }
}