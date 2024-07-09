package com.example.week2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.week2.data.AppRoomDatabase
import com.example.week2.data.item.ItemRepository
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InfoActivity : AppCompatActivity() {
    private lateinit var repository: ItemRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val db = AppRoomDatabase.getDatabase(applicationContext, CoroutineScope(Dispatchers.IO))
        repository = ItemRepository(db.itemDao())

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val IdTextView: TextView = findViewById(R.id.info_id)
        val nameTextView: TextView = findViewById(R.id.info_name)
        val logoutButton: Button = findViewById(R.id.logout_button)

        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val myLoginId = sharedPref.getString("login_id", null)
        if (myLoginId != null) {
            IdTextView.text = myLoginId
        } else {
            Log.d("myLoginId", "login 아이디가 없음")
        }
        val myName = sharedPref.getString("nickname", null)
        if (myName != null) {
            nameTextView.text = myName
        } else {
            Log.d("myName", "nickname이 없음")
        }

        logoutButton.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e("Logout", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                } else {
                    Log.i("Logout", "로그아웃 성공. SDK에서 토큰 삭제됨")
                    val editor = sharedPref.edit()
                    editor.clear()
                    editor.apply()

                    CoroutineScope(Dispatchers.IO).launch {
                        repository.deleteAll() // RoomDB 초기화
                        Log.d("Logout", "All items deleted from the local database")
                    }

                    val intent = Intent(this@InfoActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteAll() // RoomDB 초기화
            Log.d("Logout", "All items deleted from the local database")
        }

        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear() // SharedPreferences 초기화
            apply()
        }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
