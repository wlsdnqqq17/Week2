package com.example.week2

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class User(
    val login_id: String,
    val nickname: String
)

data class UserResponse(
    val success:Boolean,
    val message: String
)

interface ApiService {
    @POST("save_kakao_user/")
    fun saveKakaoUser(@Body user: User): Call<Void>

    @GET("search_user/")
    fun searchUser(@Query("loginId") login_id: String): Call<UserResponse>
}

