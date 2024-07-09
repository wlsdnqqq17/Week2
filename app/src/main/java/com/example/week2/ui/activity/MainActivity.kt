package com.example.week2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val kakaoLoginButton: ImageButton = findViewById(R.id.kakao_login_button)
        /*------For Test-------*/
        val intent = Intent(this@MainActivity, HomePageActivity::class.java)
        startActivity(intent)
        /*------For Test-------*/
        kakaoLoginButton.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e("KakaoLogin", "로그인 실패", error)
                    } else if (token != null) {
                        Log.i("KakaoLogin", "로그인 성공")
                        UserApiClient.instance.me { user, error ->
                            if (error != null) {
                                Log.e("KakaoLogin", "사용자 정보 요청 실패", error)
                            } else if (user != null) {
                                val loginId = user.id.toString()
                                val nickname = user.kakaoAccount?.profile?.nickname
                                if (nickname != null) {
                                    sendUserToServer(loginId, nickname)
                                }
                            }
                        }
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                    if (error != null) {
                        Log.e("KakaoLogin", "로그인 실패", error)
                    } else if (token != null) {
                        Log.i("KakaoLogin", "로그인 성공")
                        UserApiClient.instance.me { user, error ->
                            if (error != null) {
                                Log.e("KakaoLogin", "사용자 정보 요청 실패", error)
                            } else if (user != null) {
                                val loginId = user.id.toString()
                                val nickname = user.kakaoAccount?.profile?.nickname
                                if (nickname != null) {
                                    sendUserToServer(loginId, nickname)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun sendUserToServer(loginId: String, nickname: String) {
        val retrofit = RetrofitClient.getInstance()
        val apiService = retrofit.create(ApiService::class.java)
        val user = User(loginId, nickname)

        Log.i("SendUserToServer", "Sending user data: $loginId, $nickname")

        apiService.saveKakaoUser(user).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.i("ServerResponse", "User data sent successfully")
                    val intent = Intent(this@MainActivity, HomePageActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.e("ServerResponse", "Failed to send user data. Error code: ${response.code()}, Error body: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ServerResponse", "Error: ${t.message}")
            }
        })
    }
}
