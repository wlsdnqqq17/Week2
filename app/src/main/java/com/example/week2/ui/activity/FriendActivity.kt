package com.example.week2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var friendAdapter: FriendAdapter
    private var friendList: MutableList<User> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)

        val toolbar: Toolbar = findViewById(R.id.friend_toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val friendRecyclerView: RecyclerView = findViewById(R.id.friend_recycler_view)
        friendRecyclerView.layoutManager = LinearLayoutManager(this)
        val friendAdapter = FriendAdapter(friendList)
        friendRecyclerView.adapter = friendAdapter

        val addButton: ImageButton = findViewById(R.id.friend_add_button)
        addButton.setOnClickListener {
            val intent = Intent(this, FriendAddActivity::class.java)
            startActivity(intent)
        }

        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val myLoginId = sharedPref.getString("login_id", null)
        if (myLoginId != null) {
            getFriends(myLoginId)
        } else {
            Log.d("myLoginId", "login 아이디가 없음")
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun getFriends(userId: String) {
        apiService.getFriends(userId).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    friendList.clear()
                    response.body()?.let { friendList.addAll(it) }
                    friendAdapter.notifyDataSetChanged()
                } else {
                    Log.d("getFriends", "getFriends: 실패")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.d("getFriends", "getFriends: 실패")
            }
        })
    }

}
