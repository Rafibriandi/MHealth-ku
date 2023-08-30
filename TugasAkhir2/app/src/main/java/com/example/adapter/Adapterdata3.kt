package com.example.adapter
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tugasakhir2.*
import com.google.gson.Gson
import org.json.JSONObject

class Adapterdata3(private val context: Context) :
    RecyclerView.Adapter<Adapterdata3.Dataviewholder>() {

    private val dataList: MutableList<data3> = mutableListOf()
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    init {
        sharedPreferences = context.getSharedPreferences("PREFS_Pasien", Context.MODE_PRIVATE)
    }

    val idPasien = sharedPreferences.getString("Idpasien", "")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Dataviewholder {
        return Dataviewholder(
            LayoutInflater.from(context).inflate(R.layout.isirecycle, parent, false)
        )
    }

    override fun onBindViewHolder(holder: Dataviewholder, position: Int) {
        holder.bindModel(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    fun setData(data: List<data3>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }
    fun getData(): List<data3> {
        return dataList
    }

    inner class Dataviewholder(item: View) : RecyclerView.ViewHolder(item) {
        private val baris1: TextView = item.findViewById(R.id.baris1)
        private val baris2: TextView = item.findViewById(R.id.baris2)
        private val deleteButton: Button = item.findViewById(R.id.deleteButton)
        private val cv: CardView = item.findViewById(R.id.cv)

        fun bindModel(data: data3) {
            cv.setOnClickListener {
                val intent = Intent(context, Detail3Activity::class.java).apply {
                    putExtra("trimester", data.trimester)
                    putExtra("beratbadan", data.beratbadan)
                    putExtra("lila", data.lila)
                    putExtra("imt", data.imt)
                    putExtra("hb", data.hb)
                    putExtra("tensidarah", data.tensidarah)
                    putExtra("rot", data.rot)
                    putExtra("map", data.map)
                    putExtra("skorrisiko", data.skorrisiko)
                    putExtra("statusimunisasitt", data.statusimunisasiTT)
                    putExtra("pemberianimunisasi", data.pemberianimunisasi)
                    // Tambahkan atribut lainnya sesuai dengan data JSON Anda
                }
                context.startActivity(intent)
            }

            deleteButton.setOnClickListener {
                val alertDialog = AlertDialog.Builder(context)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Apakah Anda yakin ingin menghapus data ini?")
                    .setPositiveButton("Ya") { dialog, which ->
                        if (idPasien != null) {
                            performDelete(idPasien, data)
                        }
                    }
                    .setNegativeButton("Tidak") { dialog, which ->
                        dialog.dismiss()
                    }
                    .create()

                alertDialog.show()
            }
            baris1.text = "Trimester: ${data.trimester}"
            baris2.text = "Skor Risiko: ${data.skorrisiko}"
        }
    }

    private fun performDelete(idPasien: String, data: data3) {
        val deleteUrl = "https://18319028tugasakhir.com/deleteisiselanjutnya.php?Idpasien=${idPasien}"
        val requestMethod = Request.Method.DELETE

        val requestQueue = Volley.newRequestQueue(context)

        val stringRequest = object : StringRequest(
            requestMethod, deleteUrl,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val status = jsonObject.getString("status")
                if (status == "OK") {
                    deleteData(data)
                    Toast.makeText(context, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    context,
                    "Terjadi kesalahan: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        ) {}

        requestQueue.add(stringRequest)
    }

    private fun deleteData(data: data3) {
        dataList.remove(data)
        notifyDataSetChanged()
    }

    fun loadFromAPI2(idPasien: String) {
        if (idPasien.isEmpty()) {
            dataList.clear()
            notifyDataSetChanged()
            Toast.makeText(context, "Data masih kosong", Toast.LENGTH_SHORT).show()
        } else {
            val url = "https://18319028tugasakhir.com/getpengukuranpasien2.php?Idpasien=$idPasien"

            val requestQueue = Volley.newRequestQueue(context)

            val stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
                    val serverResponse = JSONObject(response).getJSONArray("server_response")
                    dataList.clear()
                    for (i in 0 until serverResponse.length()) {
                        val item = serverResponse.getJSONObject(i)
                        val trimester = if (item.has("Trimester")) item.getString("Trimester") else ""
                        val beratBadan = if (item.has("Beratbadan")) item.getString("Beratbadan") else ""
                        val lila = if (item.has("LiLa")) item.getString("LiLa") else ""
                        val imt = if (item.has("IMT")) item.getString("IMT") else ""
                        val hb = if (item.has("HB")) item.getString("HB") else ""
                        val tensiDarah = if (item.has("TensiDarah")) item.getString("TensiDarah") else ""
                        val rot = if (item.has("ROT")) item.getString("ROT") else ""
                        val map = if (item.has("MAP")) item.getString("MAP") else ""
                        val skorRisiko = if (item.has("SkorRisiko")) item.getString("SkorRisiko") else ""
                        val statusImunisasiTT = if (item.has("StatusImunisasiTT")) item.getString("StatusImunisasiTT") else ""
                        val pemberianImunisasi = if (item.has("PemberianImunisasi")) item.getString("PemberianImunisasi") else ""
                        val dataItem = data3(
                            trimester = trimester,
                            beratbadan = beratBadan,
                            lila = lila,
                            imt = imt,
                            hb = hb,
                            tensidarah = tensiDarah,
                            rot = rot,
                            map = map,
                            skorrisiko = skorRisiko,
                            statusimunisasiTT = statusImunisasiTT,
                            pemberianimunisasi = pemberianImunisasi
                            // Tambahkan atribut lainnya sesuai dengan data JSON Anda
                        )
                        dataList.add(dataItem)

                    }
                    if (dataList.isEmpty()) {
                        // Jika dataList kosong, tampilkan notifikasi "Belum ada data untuk ID Bidan yang diinputkan"
                        Toast.makeText(context, "Belum ada data untuk ID Bidan yang diinputkan", Toast.LENGTH_SHORT).show()
                    } else {
                        notifyDataSetChanged()
                    }

                },
                { error ->
                    Toast.makeText(
                        context,
                        "Terjadi kesalahan: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
            requestQueue.add(stringRequest)
        }
    }
}
