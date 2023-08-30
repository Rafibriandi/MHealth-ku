package com.example.tugasakhir2pasien2
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class HasilQuizActivity : AppCompatActivity() {

    private lateinit var scoreTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hasilquiz)

        scoreTextView = findViewById(R.id.nilaiquiz)

        val score = intent.getIntExtra("score", 0)
        val maxScore = intent.getIntExtra("maxScore", 0)

        scoreTextView.text = "$score/$maxScore"
    }
}