package com.example.week2

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class User(val login_id: String, val nickname: String)

interface ApiService {
    @POST("MyAvatar/save_kakao_user/")
    fun saveKakaoUser(@Body user: User): Call<Void>
}

