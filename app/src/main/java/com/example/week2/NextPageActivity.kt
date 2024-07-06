package com.example.week2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class NextPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.next_page)

        // next_page.xml에 정의된 뷰에 접근
        val statusTextView: TextView = findViewById(R.id.status_text)
        statusTextView.text = "9,000 / 10,000" // 예제 텍스트 변경
    }
}