package com.example.tugasakhir2pasien2



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.tugasakhir2pasien2.R

class MenuUtamaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menuutama)

        val opsiDataButton: Button = findViewById(R.id.opsi_data)
        val opsiEdukasiButton: Button = findViewById(R.id.opsi_edukasi)

        opsiDataButton.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }

        }}