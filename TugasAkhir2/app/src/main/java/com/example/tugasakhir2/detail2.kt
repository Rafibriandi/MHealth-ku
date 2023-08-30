package com.example.tugasakhir2
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tugasakhir2.databinding.PengukuranpasiendetailBinding

import org.json.JSONObject

class Detail2Activity : AppCompatActivity() {

    private lateinit var binding: PengukuranpasiendetailBinding
    var b: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PengukuranpasiendetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initview()
        //setupUpdateButton()
    }

    private fun initview() {
        b = intent.extras
        binding.trimester.text = b?.getString("trimester") ?: "Data tidak tersedia"
        binding.beratBadan.text = b?.getString("beratbadan") ?: "Data tidak tersedia"
        binding.tinggiBadan.text = b?.getString("tinggibadan") ?: "Data tidak tersedia"
        binding.lila.text = b?.getString("lila") ?: "Data tidak tersedia"
        binding.imt.text = b?.getString("imt") ?: "Data tidak tersedia"
        binding.hb.text = b?.getString("hb") ?: "Data tidak tersedia"
        binding.golDarah.text = b?.getString("goldarah") ?: "Data tidak tersedia"
        binding.tensiDarah.text = b?.getString("tensidarah") ?: "Data tidak tersedia"
        binding.rot.text = b?.getString("rot") ?: "Data tidak tersedia"
        binding.map.text = b?.getString("map") ?: "Data tidak tersedia"
        binding.pendeteksiFaktorRisiko.text = b?.getString("pendeteksifaktorrisiko") ?: "Data tidak tersedia"
        binding.skorRisiko.text = b?.getString("skorrisiko") ?: "Data tidak tersedia"
        binding.jarakKehamilan.text = b?.getString("jarakkehamilan") ?: "Data tidak tersedia"
        binding.statusImunisasiTT.text = b?.getString("statusimunisasitt") ?: "Data tidak tersedia"
        binding.pemberianImunisasi.text = b?.getString("pemberianimunisasi") ?: "Data tidak tersedia"
    }

//    private fun setupUpdateButton() {
//        binding.updateButton.setOnClickListener {
//            val updatedData = collectUpdatedData()
//            val updateUrl = "http://10.0.2.2/ta%202/update.php"
//            val requestQueue = Volley.newRequestQueue(this)
//
//            val stringRequest = object : StringRequest(
//                Request.Method.PUT, updateUrl,
//                Response.Listener { response ->
//                    val jsonObject = JSONObject(response)
//                    val status = jsonObject.getString("status")
//                    if (status == "OK") {
//                        showToast("Data berhasil di-update")
//                    } else {
//                        showToast("Gagal meng-update data")
//                    }
//                },
//                Response.ErrorListener { error ->
//                    showToast("Terjadi kesalahan: ${error.message}")
//                }
//            ) {
//                override fun getParams(): MutableMap<String, String> {
//                    return updatedData
//                }
//            }
//
//            requestQueue.add(stringRequest)
//        }
//    }

//    private fun collectUpdatedData(): MutableMap<String, String> {
//        val updatedData = mutableMapOf<String, String>()
//        updatedData["Nomorindek"] = binding.nomorindek.text.toString()
//        updatedData["BeratBadan"] = binding.beratBadan.text.toString()
//        updatedData["TinggiBadan"] = binding.tinggiBadan.text.toString()
//        updatedData["LiLa"] = binding.lila.text.toString()
//        updatedData["IMT"] = binding.imt.text.toString()
//        updatedData["HB"] = binding.hb.text.toString()
//        updatedData["GolDarah"] = binding.golDarah.text.toString()
//        updatedData["TensiDarah"] = binding.tensiDarah.text.toString()
//        updatedData["ROT"] = binding.rot.text.toString()
//        updatedData["MAP"] = binding.map.text.toString()
//        updatedData["PendeteksiFaktorRisiko"] = binding.pendeteksiFaktorRisiko.text.toString()
//        updatedData["SkorRisiko"] = binding.skorRisiko.text.toString()
//        updatedData["JarakKehamilan"] = binding.jarakKehamilan.text.toString()
//        updatedData["StatusImunisasiTT"] = binding.statusImunisasiTT.text.toString()
//        updatedData["PemberianImunisasi"] = binding.pemberianImunisasi.text.toString()
//
//        return updatedData
//    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
