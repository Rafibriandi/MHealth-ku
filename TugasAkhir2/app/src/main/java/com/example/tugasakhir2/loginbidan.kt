package com.example.tugasakhir2
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class LoginBidanActivity : AppCompatActivity() {

    private lateinit var idEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    private val urlLogin = "https://18319028tugasakhir.com/loginbidan.php"
    private val urlGetIdPuskesmasPustu = "https://18319028tugasakhir.com//getidpuskesmaspustu.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginbidan)

        idEditText = findViewById(R.id.id)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.login)

        loginButton.setOnClickListener {
            val id = idEditText.text.toString()
            val password = passwordEditText.text.toString()

            login(id, password)
        }
    }

    private fun login(id: String, password: String) {
        if (id.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Harap isi seluruh kolom", Toast.LENGTH_SHORT).show()
            return
        }

        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Request.Method.POST, urlLogin,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val serverResponseArray = jsonObject.getJSONArray("server_response")
                val firstObjectInArray = serverResponseArray.getJSONObject(0)
                val success = firstObjectInArray.getString("status")

                if (success == "OK") {

                    val sharedPref = getSharedPreferences("PREFS_Bidan", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("Idbidan", id)
                        apply()
                    }

                    getIdPuskesmasPustu(id)
                } else {
                    // Login gagal
                    Toast.makeText(this, "ID atau Password salah", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                // Tampilan pesan error
                Toast.makeText(this, "Error occurred: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["Idbidan"] = id
                params["Password"] = password
                return params
            }
        }

        requestQueue.add(stringRequest)
    }

    private fun getIdPuskesmasPustu(idBidan: String) {
        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Request.Method.GET, "$urlGetIdPuskesmasPustu?Idbidan=$idBidan",
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val serverResponseObject = jsonObject.getJSONObject("server_response")

                val idPuskesmas = serverResponseObject.getString("IDPuskesmas")
                val idPustu = serverResponseObject.getString("IDPustu")

                // Save ID Puskesmas and ID Pustu in separate SharedPrefs
                val sharedPrefPuskesmas = getSharedPreferences("PREFS_Puskesmas", Context.MODE_PRIVATE)
                with(sharedPrefPuskesmas.edit()) {
                    putString("Idpuskesmas", idPuskesmas)
                    apply()
                }

                val sharedPrefPustu = getSharedPreferences("PREFS_Pustu", Context.MODE_PRIVATE)
                with(sharedPrefPustu.edit()) {
                    putString("Idpustu", idPustu)
                    apply()
                }

                showTermsAndAgreementDialog()
            },
            Response.ErrorListener { error ->
                // Show error message
                Toast.makeText(this, "Error occurred: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {}

        requestQueue.add(stringRequest)
    }

    private fun showTermsAndAgreementDialog() {
        val message = "Terms and Agreement:\n" +
                "1. Data diisi atas persetujuan dan pengetahuan bidan.\n" +
                "2. Pengisian data dilakukan dengan jujur dan akurat"

        val dialog = AlertDialog.Builder(this)
            .setTitle("Terms and Agreement")
            .setMessage(message)
            .setPositiveButton("Accept") { _, _ ->

                val intent = Intent(this, MenuUtamaActivity::class.java)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Deny") { dialog, _ ->

                dialog.dismiss()
            }
            .create()

        dialog.show()
    }



}
