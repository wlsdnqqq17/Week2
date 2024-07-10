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

data class ServerItem(
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

data class PurchaseItemRequest(
    val user_id: String,
    val item_id: Int
)

data class PurchaseItemResponse(
    val status: String,
    val error: String?
)

data class UpdateAvatarStateRequest(
    val user_id: String,
    val item_id: Int?
)

data class UpdateAvatarStateResponse(
    val status: String,
    val created: Boolean
)

data class AvatarStateResponse(
    val status: String,
    val avatar_state: AvatarState?
)

data class AvatarState(
    val user_id: String?,
    val hat_item_id: Int?,
    val clothes_item_id: Int?,
    val accessory_item_id: Int?,
    val background_item_id: Int?
)

data class ItemImageUrlResponse(
    val status: String,
    val data: ItemImageUrl?
)

data class ItemImageUrl(
    val item_id: Int,
    val item_image_url: String
)

interface ApiService {
    @POST("save_kakao_user/")
    fun saveKakaoUser(@Body user: User): Call<Void>

    @GET("search_user/")
    fun searchUser(@Query("loginId") login_id: String): Call<UserResponse>

    @GET("get_items/")
    fun getShopItems(): Call<List<ServerItem>>

    @POST("add_friend/")
    fun addFriend(@Body friendRequest: FriendRequest): Call<Void>

    @GET("get_friends/{user_id}/")
    fun getFriends(@Path("user_id") userId: String): Call<List<User>>

    @GET("get_friend_requests/{user_id}/")
    fun getFriendRequests(@Path("user_id") userId: String): Call<List<User>>

    @POST("accept_friend_request/")
    fun acceptFriendRequest(@Body request: FriendRequest): Call<Void>

    @POST("purchase_item/")
    fun purchaseItem(@Body request: PurchaseItemRequest): Call<PurchaseItemResponse>

    @GET("user_items/")
    fun getUserItems(@Query("user_id") userId: String): Call<List<Item>>

    @POST("update_avatar_state/")
    fun updateAvatarState(@Body request: UpdateAvatarStateRequest): Call<UpdateAvatarStateResponse>

    @GET("get_avatar_state/")
    fun getAvatarState(@Query("user_id") userId: String): Call<AvatarStateResponse>

    @GET("get_item_image_url/")
    fun getItemImageUrl(@Query("item_id") itemId: Int): Call<ItemImageUrlResponse>

}

