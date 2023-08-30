package com.example.tugasakhir2
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class MenuUtamaActivity : AppCompatActivity() {
    private lateinit var textviewkiri: TextView
    private lateinit var textviewkanan: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menuutama)
        val sharedPref = getSharedPreferences("PREFS_Bidan", Context.MODE_PRIVATE)
        val Idbidan = sharedPref.getString("Idbidan","")

        textviewkiri = findViewById(R.id.textviewkiri)
        textviewkanan = findViewById(R.id.textviewkanan)

        val opsiDataButton: Button = findViewById(R.id.opsi_data)
        val opsiEdukasiButton: Button = findViewById(R.id.opsi_edukasi)
        val logoutButton: Button = findViewById(R.id.logout)
        val opsiEdukasiBidanButton : Button =  findViewById(R.id.opsi_edukasi_bidan)

            // textviewkanan.text = "ID Bidan :\n ${Idbidan}"

        opsiDataButton.setOnClickListener {
            val intent = Intent(this, IdentitasPasienActivity::class.java)
            startActivity(intent)
        }

        opsiEdukasiButton.setOnClickListener {
            val intent = Intent(this, EdukasiPasienActivity::class.java)
            startActivity(intent)
        }
        opsiEdukasiBidanButton.setOnClickListener{
            val intent = Intent(this, EdukasiBidanActivity::class.java)
            startActivity(intent)

        }

        logoutButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Apakah anda yakin mau logout?")
                .setPositiveButton("Ya") { _, _ ->
                    // Ganti ini dengan mekanisme logout Anda
                    val intent = Intent(this, LoginBidanActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        // Mengambil data dari server menggunakan PHP
        if (Idbidan != null) {
            getDataFromServer(Idbidan)
            getNamaPustuFromServer(Idbidan) // Tambahkan pemanggilan untuk fungsi getNamaPuskesmasFromServer
        }
    }
    private fun getNamaPustuFromServer(Idbidan: String) {
        val url = " https://18319028tugasakhir.com/getnamapustu.php?Idbidan=$Idbidan"
        val queue = Volley.newRequestQueue(this)
        val sharedPrefPustu = getSharedPreferences("PREFS_Pustu", Context.MODE_PRIVATE)
        var idPustu = sharedPrefPustu.getString("Idpustu", "")
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val serverResponse = response.getJSONArray("server_response")
                    if (serverResponse.length() > 0) {
                        val data = serverResponse.getJSONObject(0)
                        val value = data.getString("NamaPustu")
                        textviewkanan.text = "Nama Pustu:\n${value}"
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            })

        queue.add(request)
    }

    private fun getDataFromServer(Idbidan : String) {
        val url = "https://18319028tugasakhir.com/getnamabidan.php?Idbidan=$Idbidan"
        val queue = Volley.newRequestQueue(this)

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val serverResponse = response.getJSONArray("server_response")
                    if (serverResponse.length() > 0) {
                        val data = serverResponse.getJSONObject(0)
                        val value = data.getString("NamaBidan")
                        textviewkiri.text = "Halo,\n${value}"
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            })

        queue.add(request)
    }



}


