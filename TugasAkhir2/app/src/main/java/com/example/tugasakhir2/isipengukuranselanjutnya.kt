package com.example.tugasakhir2
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class IsiPengukuranSelanjutnyaActivity : AppCompatActivity() {
    private lateinit var trimester: Spinner
    private lateinit var beratBadan: EditText
    private lateinit var lila: EditText
    private lateinit var imt: EditText
    private lateinit var hb: EditText
    private lateinit var tensiDarah: EditText
    private lateinit var rot: EditText
    private lateinit var map: EditText
    private lateinit var statusImunisasiTT: Spinner
    private lateinit var pemberianImunisasi: EditText
    private lateinit var simpanButton: Button

    companion object {
        const val REQUEST_NEXT2 = 4
        const val EXTRA_ID_PASIEN = "Idpasien"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.isitrimester2)
        trimester = findViewById(R.id.trimester)
        beratBadan = findViewById(R.id.berat_badan)

        lila = findViewById(R.id.lila)
        imt = findViewById(R.id.imt)
        hb = findViewById(R.id.hb)

        tensiDarah = findViewById(R.id.tensi_darah)
        rot = findViewById(R.id.rot)
        map = findViewById(R.id.map)
        statusImunisasiTT = findViewById(R.id.status_imunisasi_tt)
        pemberianImunisasi = findViewById(R.id.pemberian_imunisasi)
        simpanButton = findViewById(R.id.simpan_button)

        val sharedPref = getSharedPreferences("PREFS_Trimester", Context.MODE_PRIVATE)
        val nomorSatu = sharedPref.getString("Nomor", "")!!
        val sharedPreferences2 = getSharedPreferences("PREFS2", Context.MODE_PRIVATE)







        // Set up status imunisasi TT Spinner
        val imunisasiTTOptions = arrayOf("Status Imunisasi TT", "TT1", "TT2", "TT3", "TT4", "TT5")
        val imunisasiTTAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, imunisasiTTOptions)
        statusImunisasiTT.adapter = imunisasiTTAdapter
        statusImunisasiTT.setSelection(0, false) // Tidak ada yang dipilih secara default
        val TrimesterOptions = arrayOf("Trimester","1","2", "3" )
        val TrimesterAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, TrimesterOptions)
        trimester.adapter = TrimesterAdapter
        trimester.setSelection(0, false) // Tidak ada yang dipilih secara default

        // Set up input type for numeric fields
        beratBadan.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        lila.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        imt.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        hb.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

        // Set up pemberian imunisasi EditText as non-focusable
        pemberianImunisasi.isFocusable = false
        trimester.isFocusable = false

        // Set up DatePickerDialog for pemberian imunisasi
        pemberianImunisasi.setOnClickListener {
            showDatePickerDialog()
        }

        simpanButton.setOnClickListener {
            val selectedStatusImunisasi = statusImunisasiTT.selectedItem.toString()
            val selectedTrimester = trimester.selectedItem.toString()



            val sharedPrefData = getSharedPreferences("PREFS_TRIMESTER2", Context.MODE_PRIVATE)
            val editor = sharedPrefData.edit()
            editor.putString("Trimester", selectedTrimester)
            editor.putString("BeratBadan", beratBadan.text.toString())
            editor.putString("LiLa", lila.text.toString())
            editor.putString("IMT", imt.text.toString())
            editor.putString("HB", hb.text.toString())
            editor.putString("TensiDarah", tensiDarah.text.toString())
            editor.putString("ROT", rot.text.toString())
            editor.putString("MAP", map.text.toString())
            editor.putString("StatusImunisasiTT", selectedStatusImunisasi)
            editor.putString("PemberianImunisasi", pemberianImunisasi.text.toString())
            editor.apply()

            if (
                selectedStatusImunisasi == "Status Imunisasi TT" || selectedTrimester == "Trimester"||
                        isAnyFieldEmpty()
            ) {
                Toast.makeText(this, "harap isi seluruh kolom", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, KSPRTrimester23Activity::class.java)
                startActivityForResult(intent, REQUEST_NEXT2)
            }

        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(calendar.time)
                pemberianImunisasi.setText(formattedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun isAnyFieldEmpty(): Boolean {
        return beratBadan.text.isNullOrEmpty() ||
                lila.text.isNullOrEmpty() ||
                imt.text.isNullOrEmpty() ||
                hb.text.isNullOrEmpty() ||
                tensiDarah.text.isNullOrEmpty() ||
                rot.text.isNullOrEmpty() ||
                map.text.isNullOrEmpty() ||
                pemberianImunisasi.text.isNullOrEmpty()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IsiPengukuranSelanjutnyaActivity.REQUEST_NEXT2 && resultCode == Activity.RESULT_OK) {
            val idPasien = data?.getStringExtra(KSPRTrimester23Activity.EXTRA_ID_PASIEN)
            val intent = Intent().apply {
                putExtra(IsiPengukuranSelanjutnyaActivity.EXTRA_ID_PASIEN, idPasien)
            }

            setResult(Activity.RESULT_OK, intent)
            finish()

        }
    }
}

