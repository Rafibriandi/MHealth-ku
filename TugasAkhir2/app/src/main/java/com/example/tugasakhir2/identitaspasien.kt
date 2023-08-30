package com.example.tugasakhir2
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.Adapterdata
import com.example.adapter.Adapterdata2
import com.example.tugasakhir2.data
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class IdentitasPasienActivity : AppCompatActivity() {

    private lateinit var adapter: Adapterdata
    private lateinit var adapter2: Adapterdata2
    private lateinit var recyclerView: RecyclerView
    private lateinit var tambahButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var filterButton: Button
    private lateinit var searchView: SearchView

    private lateinit var originalData: List<data>

    companion object {
        const val REQUEST_NEXT = 3
        const val SHARED_PREFS_NAME = "PREFAdapteridentitaspasien"
        const val KEY_ADAPTER_DATA = "AdapterDataKey"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tampilandatapasien)
        val sharedPrefBidan = getSharedPreferences("PREFS_Bidan", Context.MODE_PRIVATE)
        var Idbidan =
            sharedPrefBidan.getString("Idbidan", "")

        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

        adapter = Adapterdata(this)
        adapter2 = Adapterdata2(this)
        recyclerView = findViewById(R.id.view)
        tambahButton = findViewById(R.id.tambah_data_button)
        filterButton = findViewById(R.id.filter_button)
        searchView = findViewById(R.id.search_bar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Mengambil data adapter dari SharedPreferences saat onCreate asumsi sudah ada data
        originalData = getAdapterDataFromSharedPreferences() ?: emptyList()
        adapter.setData(originalData)

        tambahButton.setOnClickListener {
            val intent = Intent(this, IsiIdentitasPasienActivity::class.java)
            startActivityForResult(intent, REQUEST_NEXT)
        }

        filterButton.setOnClickListener {
            showFilterDialog()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredData = filterDataByIdPasien(newText, filterOption)
                adapter.setData(filteredData)
                return true
            }
        })

        if (Idbidan != null) {
            adapter.loadFromAPI(Idbidan)
        }
    }

    private var filterOption: String = ""

    private fun showFilterDialog() {
        val filterOptions = arrayOf("Skor Tinggi", "Skor Rendah")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Filter Skor Risiko")
        builder.setSingleChoiceItems(filterOptions, -1) { _, which ->
            filterOption = filterOptions[which]
        }
        builder.setPositiveButton("Filter") { _, _ ->
            val filteredData = filterDataByIdPasien(searchView.query.toString(), filterOption)
            adapter.setData(filteredData)
        }
        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun filterDataByIdPasien(query: String?, filterOption: String): List<data> {
        if (query.isNullOrEmpty()) {
            // Gunakan data asli dari API jika query kosong
            return adapter.getOriginalDataFromAPI()
        }

        val filteredList = adapter.getData().filter { item ->
            item.IDPasien.toLowerCase(Locale.getDefault())
                .contains(query.toLowerCase(Locale.getDefault()))
        }

        // Jika filterOption adalah "Skor Tinggi" atau "Skor Rendah", tambahkan filter berdasarkan skor risiko
        if (filterOption == "Skor Tinggi" || filterOption == "Skor Rendah") {
            val skorTinggi = filterOption == "Skor Tinggi"
            val adapterData2List = adapter2.getData()
1
            val filteredData = filteredList.filter { item ->
                val data2Item = adapterData2List.find { it.trimester == item.IDPasien }
                (data2Item?.skorrisiko?.toIntOrNull() ?: 0) > 5 == skorTinggi
            }

            return filteredData
        }
10
        return filteredList
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_NEXT && resultCode == Activity.RESULT_OK) {
            val Idbidan = data?.getStringExtra("IDBidan")
            if (Idbidan != null) {
                adapter.loadFromAPI(Idbidan)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        // Save the current adapter data to SharedPreferences when the app is paused
        saveAdapterDataToSharedPreferences(adapter.getData())
    }

    private fun saveAdapterDataToSharedPreferences(data: List<data>) {
        val json = Gson().toJson(data)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_ADAPTER_DATA, json)
        editor.apply()
    }

    private fun getAdapterDataFromSharedPreferences(): List<data>? {
        val json = sharedPreferences.getString(KEY_ADAPTER_DATA, null)
        return if (json != null) {
            val type = object : TypeToken<List<data>>() {}.type
            Gson().fromJson(json, type)
        } else {
            null
        }
    }
}
