package com.example.tugasakhir2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tugasakhir2.databinding.IdentitaspasiendetailBinding
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: IdentitaspasiendetailBinding
    var b: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = IdentitaspasiendetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initview()



        binding.datapengukuranbutton.setOnClickListener(){
            val intent = Intent(this,PilihPengisianActivity::class.java)
            startActivity(intent)
        }

    }

    fun initview() {
        b = intent.extras
        binding.nik.text = b?.getString("NIK") ?: "Data tidak tersedia"
        binding.idPasien.text =  b?.getString("IDPasien") ?: "Data tidak tersedia"
        binding.namaPasien.text = b?.getString("NamaPasien") ?: "Data tidak tersedia"
        binding.namaSuami.text = b?.getString("NamaSuami") ?: "Data tidak tersedia"
        binding.noTelepon.text =  b?.getString("NoTelepon") ?: "Data tidak tersedia"
        binding.alamat.text = b?.getString("Alamat") ?: "Data tidak tersedia"
        binding.umurPasien.text = b?.getString("UmurPasien") ?: "Data tidak tersedia"
        binding.hpht.text = b?.getString("HPHT") ?: "Data tidak tersedia"
        binding.usiaKehamilan.text = b?.getString("UsiaKehamilan") ?: "Data tidak tersedia"
        binding.hariPerkiraanLahir.text = b?.getString("HariPerkiraanLahir") ?: "Data tidak tersedia"
        binding.hamilKe.text = b?.getString("HamilKe") ?: "Data tidak tersedia"

        val sharedPreferences3 = getSharedPreferences("PREFS_Pasien", Context.MODE_PRIVATE)
        with(sharedPreferences3.edit()) {
            putString("Idpasien", binding.idPasien.text.toString())
            apply()
        }
    }



}









