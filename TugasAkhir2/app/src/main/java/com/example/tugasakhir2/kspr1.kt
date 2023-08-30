package com.example.tugasakhir2
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class KSPRTrimester1Activity : AppCompatActivity() {
    private lateinit var textViewScore: TextView
    private lateinit var simpanButton: Button
    private val urlFinal = "https://18319028tugasakhir.com/isipertama.php"
    // New URL for the different server
    private val urlNew = "https://18319028tugasakhir.com/kspr.php"

    private val factorScores: MutableMap<Int, Int> = mutableMapOf()

    companion object {
        const val EXTRA_SKOR_RISIKO = "extraSkorRisiko"
        const val EXTRA_ID_PASIEN = "Idpasien"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kspr)

        textViewScore = findViewById(R.id.textview_score)
        simpanButton = findViewById(R.id.simpan_buttoni)
        setCheckBoxOnClickListener(R.id.satu, 4);
        setCheckBoxOnClickListener(R.id.dua, 4);
        setCheckBoxOnClickListener(R.id.dua_b, 4);
        setCheckBoxOnClickListener(R.id.tiga, 4);
        setCheckBoxOnClickListener(R.id.empat, 4);
        setCheckBoxOnClickListener(R.id.lima, 4);
        setCheckBoxOnClickListener(R.id.enam, 4);
        setCheckBoxOnClickListener(R.id.tujuh, 4);
        setCheckBoxOnClickListener(R.id.delapan, 4);
        setCheckBoxOnClickListener(R.id.sembilan_a, 4);
        setCheckBoxOnClickListener(R.id.sembilan_b, 4);
        setCheckBoxOnClickListener(R.id.sembilan_c, 4);
        setCheckBoxOnClickListener(R.id.sepuluh, 8);
        setCheckBoxOnClickListener(R.id.sebelas_a, 2);
        setCheckBoxOnClickListener(R.id.sebelas_b, 2);
        setCheckBoxOnClickListener(R.id.sebelas_c, 2);
        setCheckBoxOnClickListener(R.id.sebelas_d, 2);
        setCheckBoxOnClickListener(R.id.sebelas_e, 4);
        setCheckBoxOnClickListener(R.id.sebelas_f, 4);
        setCheckBoxOnClickListener(R.id.duabelas, 4);
        setCheckBoxOnClickListener(R.id.tigabelas, 4);
        setCheckBoxOnClickListener(R.id.empatbelas, 4);
        setCheckBoxOnClickListener(R.id.limabelas, 4);
        setCheckBoxOnClickListener(R.id.enambelas, 4);
        setCheckBoxOnClickListener(R.id.tujuhbelas, 8);
        setCheckBoxOnClickListener(R.id.delapanbelas, 8);
        setCheckBoxOnClickListener(R.id.sembilanbelas, 8);
        setCheckBoxOnClickListener(R.id.duapuluh, 8);

        simpanButton.setOnClickListener {
            saveScore()
        }
    }

    // Function to set click listeners for each checkbox
    private fun setCheckBoxOnClickListener(checkboxId: Int, factorScore: Int) {
        val checkBox = findViewById<CheckBox>(checkboxId)
        checkBox.setOnClickListener {
            factorScores[checkboxId] = getScoreForFactor(checkBox.isChecked, factorScore)
            updateTotalScore()
        }
    }


    // Function to calculate and update the total score
    private fun updateTotalScore() {
        var totalScore = 2
        for (score in factorScores.values) {
            totalScore += score
        }
        textViewScore.text = "Jumlah Skor: $totalScore"
    }

    // Function to handle saving the score and sending data using Volley
    private fun saveScore() {
        val score = textViewScore.text.toString().substringAfterLast(":").trim().toInt()
        var totalScore = 0
        for (factorScore in factorScores.values) {
            totalScore += factorScore
        }
        // Retrieve other necessary data for the request
        val sharedPref3 = getSharedPreferences("PREFS_Trimester", Context.MODE_PRIVATE)
        val nomorSatu = sharedPref3.getString("Nomor", "")!!
        val sharedPreferences = getSharedPreferences("PREFS_Bidan", Context.MODE_PRIVATE)
        val Idbidan = sharedPreferences.getString("Idbidan", "")!!
        val sharedPreferences2 = getSharedPreferences("PREFS_Pasien", Context.MODE_PRIVATE)
        val Idpasien = sharedPreferences2.getString("Idpasien", "")!!
        val sharedPreferences3 = getSharedPreferences("PREFSb", Context.MODE_PRIVATE)
        with(sharedPreferences3.edit()) {
            putString("Idpasien2", Idpasien)
            apply()
        }
        val idPasien2 = sharedPreferences3.getString("Idpasien2", "")!!

        // Create the JSON request body
        val sharedPref = getSharedPreferences("PREFS_TRIMESTER1", Context.MODE_PRIVATE)
        val beratBadanValue = sharedPref.getString("BeratBadan", "")
        val tinggiBadanValue = sharedPref.getString("TinggiBadan", "")
        val lilaValue = sharedPref.getString("LiLa", "")
        val imtValue = sharedPref.getString("IMT", "")
        val hbValue = sharedPref.getString("HB", "")
        val golDarahValue = sharedPref.getString("GolDarah", "")
        val tensiDarahValue = sharedPref.getString("TensiDarah", "")
        val rotValue = sharedPref.getString("ROT", "")
        val mapValue = sharedPref.getString("MAP", "")
        val pendeteksiFaktorRisikoValue = sharedPref.getString("PendeteksiFaktorRisiko", "")
        val jarakKehamilanValue = sharedPref.getString("JarakKehamilan", "")
        val statusImunisasiTTValue = sharedPref.getString("StatusImunisasiTT", "")
        val pemberianImunisasiValue = sharedPref.getString("PemberianImunisasi", "")

        val queue = Volley.newRequestQueue(this)
        val paramsFinal = HashMap<String, String>()
        paramsFinal["Trimester"] = nomorSatu
        paramsFinal["Idbidan"] = Idbidan
        paramsFinal["Idpasien"] =Idpasien
        paramsFinal["SkorRisiko"] = score.toString()
        paramsFinal["BeratBadan"] = beratBadanValue.toString()
        paramsFinal["TinggiBadan"] = tinggiBadanValue.toString()
        paramsFinal["LiLa"] = lilaValue.toString()
        paramsFinal["IMT"] = imtValue.toString()
        paramsFinal["HB"] = hbValue.toString()
        paramsFinal["GolDarah"] = golDarahValue.toString()
        paramsFinal["TensiDarah"] = tensiDarahValue.toString()
        paramsFinal["ROT"] = rotValue.toString()
        paramsFinal["MAP"] = mapValue.toString()
        paramsFinal["PendeteksiFaktorRisiko"] = pendeteksiFaktorRisikoValue.toString()
        paramsFinal["JarakKehamilan"] = jarakKehamilanValue.toString()
        paramsFinal["StatusImunisasiTT"] = statusImunisasiTTValue.toString()
        paramsFinal["PemberianImunisasi"] = pemberianImunisasiValue.toString()

        // Create a new HashMap to hold checkbox values along with their factorscores

        val paramsNew = HashMap<String, String>()

        // Loop through the factorScores and add each checkbox's data to the paramsFinal
        for ((viewId, factorScore) in factorScores) {
            val checkBox = findViewById<CheckBox>(viewId)
            val isChecked = checkBox.isChecked
            val scoreForCheckbox = getScoreForFactor(isChecked, factorScore)
            paramsNew["$viewId"] = scoreForCheckbox.toString()
        }
        paramsNew["Idpasien"] = Idpasien
        paramsNew["Trimester"] = nomorSatu
        paramsNew["Skortotal"] = score.toString()
        paramsNew["satu"] = factorScores[R.id.satu].toString()
        paramsNew["dua"] = factorScores[R.id.dua].toString()
        paramsNew["dua_b"] = factorScores[R.id.dua_b].toString()
        paramsNew["tiga"] = factorScores[R.id.tiga].toString()
        paramsNew["empat"] = factorScores[R.id.empat].toString()
        paramsNew["lima"] = factorScores[R.id.lima].toString()
        paramsNew["enam"] = factorScores[R.id.enam].toString()
        paramsNew["tujuh"] = factorScores[R.id.tujuh].toString()
        paramsNew["delapan"] = factorScores[R.id.delapan].toString()
        paramsNew["sembilan_a"] = factorScores[R.id.sembilan_a].toString()
        paramsNew["sembilan_b"] = factorScores[R.id.sembilan_b].toString()
        paramsNew["sembilan_c"] = factorScores[R.id.sembilan_c].toString()
        paramsNew["sepuluh"] = factorScores[R.id.sepuluh].toString()
        paramsNew["sebelas_a"] = factorScores[R.id.sebelas_a].toString()
        paramsNew["sebelas_b"] = factorScores[R.id.sebelas_b].toString()
        paramsNew["sebelas_c"] = factorScores[R.id.sebelas_c].toString()
        paramsNew["sebelas_d"] = factorScores[R.id.sebelas_d].toString()
        paramsNew["sebelas_e"] = factorScores[R.id.sebelas_e].toString()
        paramsNew["sebelas_f"] = factorScores[R.id.sebelas_f].toString()
        paramsNew["duabelas"] = factorScores[R.id.duabelas].toString()
        paramsNew["tigabelas"] = factorScores[R.id.tigabelas].toString()
        paramsNew["empatbelas"] = factorScores[R.id.empatbelas].toString()
        paramsNew["limabelas"] = factorScores[R.id.limabelas].toString()
        paramsNew["enambelas"] = factorScores[R.id.enambelas].toString()
        paramsNew["tujuhbelas"] = factorScores[R.id.tujuhbelas].toString()
        paramsNew["delapanbelas"] = factorScores[R.id.delapanbelas].toString()
        paramsNew["sembilanbelas"] = factorScores[R.id.sembilanbelas].toString()
        paramsNew["duapuluh"] = factorScores[R.id.duapuluh].toString()
        paramsNew["Skortotal"] = score.toString()
        val jsonObject = JSONObject()
        val jsonObject2 = JSONObject()
        for (key in paramsFinal.keys) {
            jsonObject.put(key, paramsFinal[key])
        }
        for (key in paramsNew.keys) {
            jsonObject2.put(key, paramsNew[key])
        }

        // Create a new JsonObjectRequest with the new URL (urlNew)
        val jsonObjectRequestFinal = JsonObjectRequest(
            Request.Method.POST, urlFinal, jsonObject,
            Response.Listener { response ->
                // Handle response from the server
                val intent = Intent().apply {
                    putExtra("Idpasien", Idpasien)
                }

                setResult(Activity.RESULT_OK, intent)
                finish()
            },
            Response.ErrorListener { error ->
                // Handle error
            }
        )

        // Create another JsonObjectRequest with the original URL (urlFinal)
        val jsonObjectRequestNew = JsonObjectRequest(
            Request.Method.POST, urlNew, jsonObject2,
            Response.Listener { response ->
                // Handle response from the server (if needed)
                null
            },
            Response.ErrorListener { error ->
                // Handle error (if needed)
                null
            }
        )

        queue.add(jsonObjectRequestNew)
        queue.add(jsonObjectRequestFinal)
    }

    private fun getScoreForFactor(isSelected: Boolean, factorScore: Int): Int {
        return if (isSelected) factorScore else 0
    }
}
