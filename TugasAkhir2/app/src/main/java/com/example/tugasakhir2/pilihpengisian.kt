package com.example.tugasakhir2

import android.content.Intent
import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity




class PilihPengisianActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pilihtrimester)
    }

    fun trimester1Activity(view: View) {
        val intent = Intent(this, PengisianPertamaActivity::class.java)
        startActivity(intent)
    }
//
    fun trimester23Activity(view: View) {
        val intent = Intent(this, PengisianSelanjutnyaActivity::class.java)
        startActivity(intent)
    }


}