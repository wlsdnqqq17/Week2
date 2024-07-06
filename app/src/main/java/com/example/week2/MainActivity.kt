package com.example.week2

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.kakao.sdk.user.UserApiClient

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val kakaoLoginButton: ImageButton = findViewById(R.id.kakao_login_button)
        kakaoLoginButton.setOnClickListener {
            // 카카오톡이 설치되어 있는지 확인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        // 로그인 실패
                        error.printStackTrace()
                    } else if (token != null) {
                        // 로그인 성공
                        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                            if (error != null) {
                                error.printStackTrace()
                            } else if (tokenInfo != null) {
                                // 다음 페이지로 이동
                                val intent = Intent(this, NextPageActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                    if (error != null) {
                        // 로그인 실패
                        error.printStackTrace()
                    } else if (token != null) {
                        // 로그인 성공
                        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                            if (error != null) {
                                error.printStackTrace()
                            } else if (tokenInfo != null) {
                                // 다음 페이지로 이동
                                val intent = Intent(this, NextPageActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                }
            }
        }
    }
}

