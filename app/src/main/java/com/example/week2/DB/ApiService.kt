package com.example.week2

import com.example.week2.data.item.Item
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

data class User(
    val login_id: String,
    val nickname: String
)

data class UserResponse (
    val success: Boolean,
    val message: String?,
    val data: User?
)

interface ApiService {
    @POST("save_kakao_user/")
    fun saveKakaoUser(@Body user: User): Call<Void>

    @GET("search_user/")
    fun searchUser(@Query("loginId") login_id: String): Call<UserResponse>

    //@GET("shop/items/")
    //fun getShopItems(): Call<List<Item>>

    @GET
    fun getShopItems(@Url url: String): Call<List<Item>>


}

