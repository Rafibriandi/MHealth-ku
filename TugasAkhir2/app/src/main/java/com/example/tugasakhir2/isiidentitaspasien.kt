package com.example.tugasakhir2
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class IsiIdentitasPasienActivity : AppCompatActivity() {
    private lateinit var nik: EditText
    private lateinit var idpasien: EditText
    private lateinit var namapasien: EditText
    private lateinit var namasuami: EditText
    private lateinit var alamat: EditText
    private lateinit var notelepon: EditText
    private lateinit var umurpasien: EditText
    private lateinit var hpht: EditText
    private lateinit var ukehamilan: EditText
    private lateinit var perkiraan: EditText
    private lateinit var hamilke: EditText
    private lateinit var tambahbutton: Button

    companion object {
        const val REQUEST_NEXT = 2
        const val EXTRA_ID_PASIEN = "Idpasien"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.datatambahanidentitas)
        val sharedPrefPuskesmas = getSharedPreferences("PREFS_Puskesmas", Context.MODE_PRIVATE)
        var idPuskesmas =
            sharedPrefPuskesmas.getString("Idpuskesmas", "") // Menggunakan nilai default ""
        val sharedPref = getSharedPreferences("PREFS_Bidan", Context.MODE_PRIVATE)
        val Idbidan = sharedPref.getString("Idbidan","")

        val sharedPrefPustu = getSharedPreferences("PREFS_Pustu", Context.MODE_PRIVATE)
        var idPustu = sharedPrefPustu.getString("Idpustu", "") // Menggunakan nilai default ""


        tambahbutton = findViewById(R.id.tambahdata_button)
        nik = findViewById(R.id.nik)
        idpasien= findViewById(R.id.id_pasien)
        namapasien = findViewById(R.id.nama_pasien)
        namasuami = findViewById(R.id.nama_suami)
        alamat = findViewById(R.id.alamat)
        notelepon = findViewById(R.id.no_telepon)
        umurpasien = findViewById(R.id.umur_pasien)
        hpht = findViewById(R.id.hpht)
        ukehamilan = findViewById(R.id.usia_kehamilan)
        perkiraan = findViewById(R.id.perkiraan_lahir)
        hamilke = findViewById(R.id.hamil_ke)
        idpasien.inputType = InputType.TYPE_NULL
        idpasien.isFocusableInTouchMode = false

        // Set keyboard input type for numeric fields
        nik.inputType = InputType.TYPE_CLASS_NUMBER
        idpasien.inputType = InputType.TYPE_NULL // Set input type to null for ID Pasien EditText
        notelepon.inputType = InputType.TYPE_CLASS_NUMBER
        umurpasien.inputType = InputType.TYPE_CLASS_NUMBER
        hamilke.inputType = InputType.TYPE_CLASS_NUMBER
        ukehamilan.inputType = InputType.TYPE_CLASS_NUMBER

        // Set non-focusable and show DatePickerDialog when EditText is touched
        hpht.isFocusable = false
        hpht.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                showDatePicker(hpht)
            }
            true
        }

        perkiraan.isFocusable = false // Set EditText perkiraan lahir tidak dapat fokus
        perkiraan.setOnClickListener(null) // Set OnClickListener perkiraan lahir menjadi null

        nik.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val nikValue = nik.text.toString()
                val idPasienValue = generateIdPasien(nikValue)
                idpasien.setText(idPasienValue)
            }
        }

        tambahbutton.setOnClickListener {
            val nikvalue = nik.text.toString()
            val idpasienvalue = idpasien.text.toString()
            val namapasienvalue = namapasien.text.toString()
            val namasuamivalue = namasuami.text.toString()
            val alamatvalue = alamat.text.toString()
            val noteleponvalue = notelepon.text.toString()
            val umurpasienvalue = umurpasien.text.toString()
            val hphtvalue = hpht.text.toString()
            val ukehamilanvalue = ukehamilan.text.toString()
            val perkiraanlahirvalue = perkiraan.text.toString()
            val hamilkevalue = hamilke.text.toString()

            if (nikvalue.isEmpty() || idpasienvalue.isEmpty() || namapasienvalue.isEmpty() ||
                namasuamivalue.isEmpty() || alamatvalue.isEmpty() || noteleponvalue.isEmpty() ||
                umurpasienvalue.isEmpty() || hphtvalue.isEmpty() || ukehamilanvalue.isEmpty() ||
                perkiraanlahirvalue.isEmpty() || hamilkevalue.isEmpty()
            ) {
                Toast.makeText(this, "harap isi seluruh kolom", Toast.LENGTH_SHORT).show()
            } else {
                val perkiraanValue = calculateHPL(hphtvalue)

                val url = "https://18319028tugasakhir.com/identitaspasien.php"

                val stringRequest = object : StringRequest(
                    Request.Method.POST, url,
                    Response.Listener<String> { response ->
                        val jsonResponse = JSONObject(response)
                        val status = jsonResponse.getJSONArray("server_response").getJSONObject(0).getString("status")

                        if (status == "OK") {
                            val resultIntent = Intent().apply {
                                putExtra("IDBidan", Idbidan)
                            }

                            setResult(Activity.RESULT_OK,resultIntent)
                            finish()
                        } else {
                            val error = jsonResponse.getJSONArray("server_response").getJSONObject(0).getString("error")
                            Toast.makeText(this@IsiIdentitasPasienActivity, "Error: $error", Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener { error ->
                        Toast.makeText(this@IsiIdentitasPasienActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }) {
                    override fun getParams(): MutableMap<String, String> {
                        val params = HashMap<String, String>()
                        params["Nik"] = nikvalue ?: ""
                        params["Idbidan"] = Idbidan ?: ""
                        params["Idpasien"] = idpasienvalue ?: ""
                        params["Puskesmas"] = idPuskesmas ?: ""
                        params["Pustu"] = idPustu ?: ""
                        params["Namapasien"] = namapasienvalue ?: ""
                        params["Namasuami"] = namasuamivalue ?: ""
                        params["Alamat"] = alamatvalue ?: ""
                        params["Notelepon"] = noteleponvalue ?: ""
                        params["Umurpasien"] = umurpasienvalue ?: ""
                        params["HPHT"] = hphtvalue
                        params["Usiakehamilan"] = ukehamilanvalue
                        params["HPL"] = perkiraanValue
                        params["Hamilke"] = hamilkevalue
                        return params
                    }
                }

                val requestQueue = Volley.newRequestQueue(this)
                requestQueue.add(stringRequest)

            }

        }
    }

    private fun calculateHPL(hphtValue: String): String {
        val hphtDate = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        hphtDate.time = formatter.parse(hphtValue)

        hphtDate.add(Calendar.DAY_OF_MONTH, 7)
        hphtDate.add(Calendar.MONTH, -3)
        hphtDate.add(Calendar.YEAR, 1)

        return formatter.format(hphtDate.time)
    }

    private fun showDatePicker(editText: EditText) {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                val selectedDate = String.format(
                    Locale.getDefault(),
                    "%04d-%02d-%02d",
                    selectedYear,
                    selectedMonth + 1,
                    selectedDay
                )
                editText.setText(selectedDate)
                perkiraan.setText(calculateHPL(selectedDate))
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun generateIdPasien(nikValue: String): String {
        val idPasien = nikValue.takeLast(5)
        return if (idPasien.length == 5) {
            idPasien
        } else {
            ""
        }
    }
}
