package com.example.week2

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class User(
    val login_id: String,
    val nickname: String
)

interface ApiService {
    @POST("save_kakao_user/")
    fun saveKakaoUser(@Body user: User): Call<Void>
}

