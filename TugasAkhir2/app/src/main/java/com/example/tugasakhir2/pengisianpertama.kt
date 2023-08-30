package com.example.tugasakhir2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.Adapterdata2
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PengisianPertamaActivity : AppCompatActivity() { // 1. inisalisasi class

    private lateinit var adapter: Adapterdata2
    private lateinit var recyclerView: RecyclerView
    private lateinit var tambahButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()
    private lateinit var sharedPrefPasien: SharedPreferences

    companion object { // 2. inisialisasi companion object
        const val REQUEST_NEXT = 3
        const val EXTRA_ID_PASIEN = "Idpasien"
        const val KEY_ADAPTER_DATA = "AdapterDataKey"
        const val SHARED_PREFS_NAME = "PREFAdapter1"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tampilanpengukuranpasien)
//3. deklarasi sharedpreferences pasien(dari detail activity)
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        sharedPrefPasien = getSharedPreferences("PREFS_Pasien", Context.MODE_PRIVATE)
        val Idpasien = sharedPrefPasien.getString("Idpasien", "")
        adapter = Adapterdata2(this)
        recyclerView = findViewById(R.id.view)
        tambahButton = findViewById(R.id.tambahdata_button)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        // Mengambil data adapter dari SharedPreferences saat onCreate
        //4. fungsi untuk ambil adapter data dari shared preferences
        val savedData = getAdapterDataFromSharedPreferences()
        if (savedData != null) {
            adapter.setData(savedData)
        }
        if (Idpasien != null) {
            adapter.loadFromAPI2(Idpasien)
        }

        tambahButton.setOnClickListener {
            val sharedPreferences2 = getSharedPreferences("PREFS_Trimester", Context.MODE_PRIVATE)
            sharedPreferences2.edit().putString("Nomor", "1").apply()

            val sharedPreferences = getSharedPreferences("PREFS_Bidan", Context.MODE_PRIVATE)
            val idBidan = sharedPreferences.getString("Idbidan", "")
            if (idBidan != null && idBidan.isNotEmpty()) {
                val intent = Intent(this, IsiPengukuranPertamaActivity::class.java)
                startActivityForResult(intent, REQUEST_NEXT)
            } else {
                Toast.makeText(this, "ID bidan not found in SharedPreferences", Toast.LENGTH_LONG).show()
                // Jika ID bidan belum ada, arahkan pengguna ke aktivitas untuk memasukkan ID bidan
                val intent = Intent(this, IsiPengukuranPertamaActivity::class.java)
                startActivityForResult(intent, REQUEST_NEXT)
                finish() // Selesaikan aktivitas ini agar pengguna tidak dapat kembali ke Trimester1Activity tanpa memasukkan ID bidan
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Menyimpan data adapter ke SharedPreferences saat onDestroy
        saveAdapterDataToSharedPreferences(adapter.getData())
    }

    private fun saveAdapterDataToSharedPreferences(data: List<data2>) {
        val Idpasien = sharedPrefPasien.getString("Idpasien", "")
        if (Idpasien != null && Idpasien.isNotEmpty()) {
            val json = gson.toJson(data)
            val editor = sharedPreferences.edit()
            editor.putString(Idpasien, json)
            editor.apply()
        }
    }

    private fun getAdapterDataFromSharedPreferences(): List<data2>? {
        val Idpasien = sharedPrefPasien.getString("Idpasien", "")
        if (Idpasien != null && Idpasien.isNotEmpty()) {
            val json = sharedPreferences.getString(Idpasien, null)
            val type = object : TypeToken<List<data2>>() {}.type
            return gson.fromJson(json, type)
        }
        return null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_NEXT && resultCode == Activity.RESULT_OK) {
            val Idpasien = data?.getStringExtra("Idpasien")
            if (Idpasien != null) {
                adapter.loadFromAPI2(Idpasien)
            }
        }
    }
}
