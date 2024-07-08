package com.example.week2

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.util.Log
import com.kakao.sdk.user.UserApiClient

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this, HomePageActivity::class.java)
        startActivity(intent)
        val kakaoLoginButton: ImageButton = findViewById(R.id.kakao_login_button)
        kakaoLoginButton.setOnClickListener {
            // 카카오톡이 설치되어 있는지 확인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        // 로그인 실패
                        Log.e("KakaoLogin", "로그인 실패", error)
                    } else if (token != null) {
                        // 로그인 성공
                        Log.i("KakaoLogin", "로그인 성공")
                        UserApiClient.instance.accessTokenInfo { tokenInfo, error1 ->
                            if (error1 != null) {
                                Log.e("KakaoLogin", "토큰 정보 조회 실패", error1)
                            } else if (tokenInfo != null) {
                                Log.i("KakaoLogin", "토큰 정보: ${tokenInfo.id}")
                                // 다음 페이지로 이동
                                val intent = Intent(this, HomePageActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                }
            } else {
                Log.i("KakaoLogin", "카카오톡 없음")
                UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                    if (error != null) {
                        // 로그인 실패
                        Log.e("KakaoLogin", "로그인 실패", error)
                    } else if (token != null) {
                        // 로그인 성공
                        Log.i("KakaoLogin", "로그인 성공")
                        UserApiClient.instance.accessTokenInfo { tokenInfo, error2 ->
                            if (error2 != null) {
                                Log.e("KakaoLogin", "토큰 정보 조회 실패", error2)
                            } else if (tokenInfo != null) {
                                Log.i("KakaoLogin", "토큰 정보: ${tokenInfo.id}")
                                // 다음 페이지로 이동
                                val intent = Intent(this, HomePageActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                }
            }
        }
    }
}
