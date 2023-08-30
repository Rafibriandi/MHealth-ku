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

class IsiPengukuranPertamaActivity : AppCompatActivity() {
    private lateinit var beratBadan: EditText
    private lateinit var tinggiBadan: EditText
    private lateinit var lila: EditText
    private lateinit var imt: EditText
    private lateinit var hb: EditText
    private lateinit var golDarah: Spinner
    private lateinit var tensiDarah: EditText
    private lateinit var rot: EditText
    private lateinit var map: EditText
    private lateinit var pendeteksiFaktorRisiko: Spinner
    private lateinit var jarakKehamilan: Spinner
    private lateinit var statusImunisasiTT: Spinner
    private lateinit var pemberianImunisasi: EditText
    private lateinit var simpanButton: Button
    private lateinit var texti: TextView

    companion object {
        const val REQUEST_NEXT2 = 4
        const val EXTRA_ID_PASIEN = "Idpasien"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.isitrimester1)
        beratBadan = findViewById(R.id.berat_badan)
        tinggiBadan = findViewById(R.id.tinggi_badan)
        lila = findViewById(R.id.lila)
        imt = findViewById(R.id.imt)
        hb = findViewById(R.id.hb)
        golDarah = findViewById(R.id.gol_darah)
        tensiDarah = findViewById(R.id.tensi_darah)
        rot = findViewById(R.id.rot)
        map = findViewById(R.id.map)
        pendeteksiFaktorRisiko = findViewById(R.id.pendeteksi_faktor_risiko)
        jarakKehamilan = findViewById(R.id.jarak_kehamilan)
        statusImunisasiTT = findViewById(R.id.status_imunisasi_tt)
        pemberianImunisasi = findViewById(R.id.pemberian_imunisasi)
        simpanButton = findViewById(R.id.simpan_button)

        val sharedPref = getSharedPreferences("PREFS_Trimester", Context.MODE_PRIVATE)
        val nomorSatu = sharedPref.getString("Nomor", "")!!
        val sharedPreferences2 = getSharedPreferences("PREFS_Pasien", Context.MODE_PRIVATE)

        // Set up golongan darah Spinner
        val golDarahOptions = arrayOf("Golongan Darah", "A", "B", "AB", "O")
        val golDarahAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, golDarahOptions)
        golDarah.adapter = golDarahAdapter
        golDarah.setSelection(0, false) // Tidak ada yang dipilih secara default

        // Set up pendeteksi faktor risiko Spinner
        val faktorRisikoOptions = arrayOf("Pendeteksi Faktor Risiko", "Nakes", "Masyarakat")
        val faktorRisikoAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, faktorRisikoOptions)
        pendeteksiFaktorRisiko.adapter = faktorRisikoAdapter
        pendeteksiFaktorRisiko.setSelection(0, false) // Tidak ada yang dipilih secara default

        // Set up jarak kehamilan Spinner
        val jarakKehamilanOptions = arrayOf("Jarak Kehamilan", "≤ 2 Tahun", "≥ 2 Tahun")
        val jarakKehamilanAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, jarakKehamilanOptions)
        jarakKehamilan.adapter = jarakKehamilanAdapter
        jarakKehamilan.setSelection(0, false) // Tidak ada yang dipilih secara default

        // Set up status imunisasi TT Spinner
        val imunisasiTTOptions = arrayOf("Status Imunisasi TT", "TT1", "TT2", "TT3", "TT4", "TT5")
        val imunisasiTTAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, imunisasiTTOptions)
        statusImunisasiTT.adapter = imunisasiTTAdapter
        statusImunisasiTT.setSelection(0, false) // Tidak ada yang dipilih secara default

        // Set up input type for numeric fields
        beratBadan.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        tinggiBadan.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        lila.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        imt.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        hb.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

        // Set up pemberian imunisasi EditText as non-focusable
        pemberianImunisasi.isFocusable = false

        // Set up DatePickerDialog for pemberian imunisasi
        pemberianImunisasi.setOnClickListener {
            showDatePickerDialog()
        }

        simpanButton.setOnClickListener {
            val selectedGolDarah = golDarah.selectedItem.toString()
            val selectedPendeteksi = pendeteksiFaktorRisiko.selectedItem.toString()
            val selectedJarakKehamilan = jarakKehamilan.selectedItem.toString()
            val selectedStatusImunisasi = statusImunisasiTT.selectedItem.toString()



            val sharedPrefData = getSharedPreferences("PREFS_TRIMESTER1", Context.MODE_PRIVATE)
            val editor = sharedPrefData.edit()
            editor.putString("BeratBadan", beratBadan.text.toString())
            editor.putString("TinggiBadan", tinggiBadan.text.toString())
            editor.putString("LiLa", lila.text.toString())
            editor.putString("IMT", imt.text.toString())
            editor.putString("HB", hb.text.toString())
            editor.putString("GolDarah", selectedGolDarah)
            editor.putString("TensiDarah", tensiDarah.text.toString())
            editor.putString("ROT", rot.text.toString())
            editor.putString("MAP", map.text.toString())
            editor.putString("PendeteksiFaktorRisiko", selectedPendeteksi)
            editor.putString("JarakKehamilan", selectedJarakKehamilan)
            editor.putString("StatusImunisasiTT", selectedStatusImunisasi)
            editor.putString("PemberianImunisasi", pemberianImunisasi.text.toString())
            editor.apply()

            if (selectedGolDarah == "Golongan Darah" ||
                selectedPendeteksi == "Pendeteksi Faktor Risiko" ||
                selectedJarakKehamilan == "Jarak Kehamilan" ||
                selectedStatusImunisasi == "Status Imunisasi TT" ||
                isAnyFieldEmpty()
            ) {
                Toast.makeText(this, "harap isi seluruh kolom", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, KSPRTrimester1Activity::class.java)
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
                tinggiBadan.text.isNullOrEmpty() ||
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
        if (requestCode == REQUEST_NEXT2 && resultCode == Activity.RESULT_OK) {
            val idPasien = data?.getStringExtra(KSPRTrimester1Activity.EXTRA_ID_PASIEN)
            val intent = Intent().apply {
                putExtra(EXTRA_ID_PASIEN, idPasien)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()

            }
        }
    }

