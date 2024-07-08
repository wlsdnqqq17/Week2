package com.example.week2

import com.example.week2.data.item.Item
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
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

data class FriendRequest(
    val from_user_id: String,
    val to_user_id: String
)

interface ApiService {
    @POST("save_kakao_user/")
    fun saveKakaoUser(@Body user: User): Call<Void>

    @GET("search_user/")
    fun searchUser(@Query("loginId") login_id: String): Call<UserResponse>

    @GET("get_items/")
    fun getShopItems(): Call<List<Item>>

    @POST("add_friend/")
    fun addFriend(@Body friendRequest: FriendRequest): Call<Void>

    @GET("get_friends/{user_id}/")
    fun getFriends(@Path("user_id") userId: String): Call<List<User>>

    @GET("get_friend_requests/{user_id}/")
    fun getFriendRequests(@Path("user_id") userId: String): Call<List<User>>

    @POST("accept_friend_request/")
    fun acceptFriendRequest(@Body request: FriendRequest): Call<Void>

}

