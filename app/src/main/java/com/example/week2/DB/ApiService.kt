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

data class Item(
    val id: Int,
    val name: String,
    val category: String,
    val item_image_url: String,
    val price: Int
)

interface ApiService {
    @POST("save_kakao_user/")
    fun saveKakaoUser(@Body user: User): Call<Void>

    @GET("search_user/")
    fun searchUser(@Query("loginId") login_id: String): Call<UserResponse>

    @GET("get_items/")
    fun getShopItems(): Call<List<Item>>
}

