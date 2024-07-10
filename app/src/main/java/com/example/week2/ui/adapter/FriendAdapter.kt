package com.example.week2

import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendAdapter(private val friendList: List<User>) : RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {
    private lateinit var apiService: ApiService

    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val friendId: TextView = itemView.findViewById(R.id.friend_item_id)
        val friendName: TextView = itemView.findViewById(R.id.friend_item_name)
        val avatarBg: ImageView = itemView.findViewById(R.id.friend_bg)
        val avatarChar: ImageView = itemView.findViewById(R.id.friend_char)
        val avatarAcc: ImageView = itemView.findViewById(R.id.friend_acc)
        val avatarHat: ImageView = itemView.findViewById(R.id.friend_hat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        apiService = RetrofitClient.getInstance().create(ApiService::class.java)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_recycler_view_item, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = friendList[position]
        holder.friendId.text = friend.login_id
        holder.friendName.text = friend.nickname

        // Assuming friend object contains URLs or resource IDs for the images
        fetchAvatarState(friend.login_id, holder)
    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    private fun fetchAvatarState(userId: String, holder: FriendViewHolder) {
        apiService.getAvatarState(userId).enqueue(object : Callback<AvatarStateResponse> {
            override fun onResponse(call: Call<AvatarStateResponse>, response: Response<AvatarStateResponse>) {
                if (response.isSuccessful) {
                    val avatarStateResponse = response.body()
                    if (avatarStateResponse != null && avatarStateResponse.avatar_state != null) {
                        Log.d("fetchAvatarState", "Parsed response: ${avatarStateResponse.avatar_state}")
                        loadAvatarImages(avatarStateResponse.avatar_state, holder)
                    } else {
                        Log.e("FetchAvatarState", "Avatar state is null")
                    }
                } else {
                    Log.e("FetchAvatarState", "Failed to fetch avatar state. Error code: ${response.code()}, Error body: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<AvatarStateResponse>, t: Throwable) {
                Log.e("FetchAvatarState", "Error: ${t.message}")
            }
        })
    }

    private fun loadAvatarImages(avatarState: AvatarState, holder: FriendViewHolder) {
        avatarState.hat_item_id?.let { loadImage(it, holder.avatarHat, "hat") }
        avatarState.clothes_item_id?.let { loadImage(it, holder.avatarChar, "clothes") }
        avatarState.accessory_item_id?.let { loadImage(it, holder.avatarAcc, "accessory") }
        avatarState.background_item_id?.let { loadImage(it, holder.avatarBg, "background") }
    }

    private fun loadImage(itemId: Int, imageView: ImageView, itemType: String) {
        apiService.getItemImageUrl(itemId).enqueue(object : Callback<ItemImageUrlResponse> {
            override fun onResponse(call: Call<ItemImageUrlResponse>, response: Response<ItemImageUrlResponse>) {
                if (response.isSuccessful) {
                    val itemImageUrlResponse = response.body()
                    Log.d("LoadImage", "Response body: $itemImageUrlResponse")
                    if (itemImageUrlResponse != null && itemImageUrlResponse.data != null) {
                        Glide.with(imageView.context)
                            .load(itemImageUrlResponse.data.item_image_url)
                            .into(imageView)
                    } else {
                        Log.e("LoadImage", "Item image URL is null for item ID: $itemId, type: $itemType")
                        loadDefaultImage(imageView, itemType)
                    }
                } else {
                    Log.e("LoadImage", "Failed to fetch item image URL. Error code: ${response.code()}, Error body: ${response.errorBody()?.string()}")
                    loadDefaultImage(imageView, itemType)
                }
            }

            override fun onFailure(call: Call<ItemImageUrlResponse>, t: Throwable) {
                Log.e("LoadImage", "Error: ${t.message}")
                loadDefaultImage(imageView, itemType)
            }
        })
    }

    private fun loadDefaultImage(imageView: ImageView, itemType: String) {
        val defaultImageRes = when (itemType) {
            "hat" -> R.drawable.empty
            "clothes" -> R.drawable.defaultchar
            "accessory" -> R.drawable.empty
            "background" -> R.drawable.empty
            else -> R.drawable.empty
        }
        Glide.with(imageView.context)
            .load(defaultImageRes)
            .into(imageView)
    }
}
