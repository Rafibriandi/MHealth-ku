package com.example.tugasakhir2pasien2

import android.content.Intent

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tugasakhir2pasien2.MenuUtamaActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var idEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        idEditText = findViewById(R.id.id)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.login)

        loginButton.setOnClickListener {
            val id = idEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (validateCredentials(id, password)) {
                // Logika setelah berhasil login (misalnya, pindah ke MainActivity)
                val intent = Intent(this, MenuUtamaActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Tampilkan pesan kesalahan jika kredensial tidak valid
                Toast.makeText(this, "ID atau password salah", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateCredentials(id: String, password: String): Boolean {
        // Logika validasi kredensial (misalnya, cek dari database)
        // Contoh sederhana: kredensial statis
        return id == "a" && password == "b"
    }
}