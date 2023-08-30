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
import com.example.adapter.Adapterdata3
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PengisianSelanjutnyaActivity : AppCompatActivity() {

    private lateinit var adapter: Adapterdata3
    private lateinit var recyclerView: RecyclerView
    private lateinit var tambahButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPrefPasien: SharedPreferences
    private val gson = Gson()

    companion object {
        const val REQUEST_NEXT = 3
        const val EXTRA_ID_PASIEN = "Idpasien"
        const val SHARED_PREFS_NAME = "PREFAdapter2"
        const val KEY_ADAPTER_DATA = "AdapterDataKey"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tampilanpengukuranpasien)

        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        sharedPrefPasien = getSharedPreferences("PREFS_Pasien", Context.MODE_PRIVATE)
        val idPasien = sharedPrefPasien.getString("Idpasien", "")

        adapter = Adapterdata3(this)
        recyclerView = findViewById(R.id.view)
        tambahButton = findViewById(R.id.tambahdata_button)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Mengambil data adapter dari SharedPreferences saat onCreate
        val savedData = getAdapterDataFromSharedPreferences()
        if (savedData != null) {
            adapter.setData(savedData)
        }
        if (idPasien != null) {
            adapter.loadFromAPI2(idPasien)
        }


        tambahButton.setOnClickListener {
            val idPasien = sharedPrefPasien.getString("Idpasien", "")
            if (idPasien != null && idPasien.isNotEmpty()) {
                val intent = Intent(this, IsiPengukuranSelanjutnyaActivity::class.java)
                startActivityForResult(intent, REQUEST_NEXT)
            } else {
                Toast.makeText(this, "ID pasien not found in SharedPreferences", Toast.LENGTH_LONG).show()
                // Jika ID pasien belum ada, arahkan pengguna ke aktivitas untuk memasukkan ID pasien
                val intent = Intent(this, IsiPengukuranSelanjutnyaActivity::class.java)
                startActivityForResult(intent, REQUEST_NEXT)
                finish() // Selesaikan aktivitas ini agar pengguna tidak dapat kembali ke Trimester23Activity tanpa memasukkan ID pasien
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Menyimpan data adapter ke SharedPreferences saat onDestroy
        saveAdapterDataToSharedPreferences(adapter.getData())
    }

    private fun saveAdapterDataToSharedPreferences(data: List<data3>) {
        val idPasien = sharedPrefPasien.getString("Idpasien", "")
        if (idPasien != null && idPasien.isNotEmpty()) {
            val json = gson.toJson(data)
            val editor = sharedPreferences.edit()
            editor.putString(idPasien, json)
            editor.apply()
        }
    }

    private fun getAdapterDataFromSharedPreferences(): List<data3>? {
        val idPasien = sharedPrefPasien.getString("Idpasien", "")
        if (idPasien != null && idPasien.isNotEmpty()) {
            val json = sharedPreferences.getString(idPasien, null)
            val type = object : TypeToken<List<data3>>() {}.type
            return gson.fromJson(json, type)
        }
        return null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_NEXT && resultCode == Activity.RESULT_OK) {
            val idPasien = data?.getStringExtra(KSPRTrimester23Activity.EXTRA_ID_PASIEN)
            if (idPasien != null) {
                adapter.loadFromAPI2(idPasien)
            }
        }
    }
}
