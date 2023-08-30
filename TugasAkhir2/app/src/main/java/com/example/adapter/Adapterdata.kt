package com.example.adapter
import android.content.Context
import android.content.Intent
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
import com.example.tugasakhir2.DetailActivity
import com.example.tugasakhir2.R
import com.example.tugasakhir2.data
import org.json.JSONObject

class Adapterdata(val context: Context) : RecyclerView.Adapter<Adapterdata.Dataviewholder>() {
    private val dataList: MutableList<data> = mutableListOf()
    private var originalDataFromAPI: List<data> = emptyList() // Tambahkan properti untuk menyimpan data asli dari API

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun getDataList(): List<data> {
        return dataList
    }

    fun setData(data: List<data>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    fun getData(): List<data> {
        return dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Dataviewholder {
        return Dataviewholder(
            LayoutInflater.from(context).inflate(R.layout.isirecycle, parent, false)
        )
    }

    override fun onBindViewHolder(holder: Adapterdata.Dataviewholder, position: Int) {
        holder.bindmodel(dataList[position])
    }

    inner class Dataviewholder(item: View) : RecyclerView.ViewHolder(item) {
        private val baris1: TextView = item.findViewById(R.id.baris1)
        private val baris2: TextView = item.findViewById(R.id.baris2)
        private val deleteButton: Button = item.findViewById(R.id.deleteButton)
        private val cv: CardView = item.findViewById(R.id.cv)

        fun bindmodel(data: data) {
            cv.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra("NIK", data.NIK)
                    putExtra("IDPasien", data.IDPasien)
                    putExtra("NamaPasien", data.NamaPasien)
                    putExtra("NamaSuami", data.NamaSuami)
                    putExtra("Alamat", data.Alamat)
                    putExtra("NoTelepon", data.NoTelepon)
                    putExtra("UmurPasien", data.UmurPasien)
                    putExtra("HPHT", data.HPHT)
                    putExtra("UsiaKehamilan", data.UsiaKehamilan)
                    putExtra("HariPerkiraanLahir", data.HariPerkiraanLahir)
                    putExtra("HamilKe", data.HamilKe)
                    // Tambahkan atribut lainnya sesuai dengan data JSON Anda
                }
                context.startActivity(intent)
            }
            baris1.text = "ID Pasien: ${data.IDPasien}"
            baris2.text = "Nama Pasien: ${data.NamaPasien}"

            deleteButton.setOnClickListener {
                val alertDialog = AlertDialog.Builder(context)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Apakah Anda yakin ingin menghapus data ini?")
                    .setPositiveButton("Ya") { dialog, _ ->
                        val idPasien = data.IDPasien
                        performDelete(idPasien, data)
                    }
                    .setNegativeButton("Tidak") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()

                alertDialog.show()
            }
        }

        private fun performDelete(idPasien: String, data: data) {
            val deleteUrl = "https://18319028tugasakhir.com/delete.php?Idpasien=${idPasien}"

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
                    Toast.makeText(context, "Terjadi kesalahan: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            ) {}

            requestQueue.add(stringRequest)
        }
    }

    private fun deleteData(data: data) {
        dataList.remove(data)
        notifyDataSetChanged()
    }

    fun loadFromAPI(idBidan: String) {
        val url = "https://18319028tugasakhir.com/getidentitaspasien.php?Idbidan=$idBidan" // Ganti dengan URL endpoint Anda

        val requestQueue = Volley.newRequestQueue(context)

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                val serverResponse = JSONObject(response).getJSONArray("server_response")
                dataList.clear()
                for (i in 0 until serverResponse.length()) {
                    val item = serverResponse.getJSONObject(i)

                    val nik = if (item.has("NIK")) item.getString("NIK") else ""
                    val idPasien = if (item.has("IDPasien")) item.getString("IDPasien") else ""
                    val namaPasien = if (item.has("NamaPasien")) item.getString("NamaPasien") else ""
                    val namaSuami = if (item.has("NamaSuami")) item.getString("NamaSuami") else ""
                    val alamat = if (item.has("Alamat")) item.getString("Alamat") else ""
                    val noTelepon = if (item.has("NoTelepon")) item.getString("NoTelepon") else ""
                    val umurPasien = if (item.has("UmurPasien")) item.getString("UmurPasien") else ""
                    val hpht = if (item.has("HPHT")) item.getString("HPHT") else ""
                    val usiaKehamilan = if (item.has("UsiaKehamilan")) item.getString("UsiaKehamilan") else ""
                    val hariPerkiraanLahir = if (item.has("HariPerkiraanLahir")) item.getString("HariPerkiraanLahir") else ""
                    val hamilKe = if (item.has("Hamilke-")) item.getString("Hamilke-") else ""

                    val dataItem = data(
                        NIK = nik,
                        IDPasien = idPasien,
                        NamaPasien = namaPasien,
                        NamaSuami = namaSuami,
                        Alamat = alamat,
                        NoTelepon = noTelepon,
                        UmurPasien = umurPasien,
                        HPHT = hpht,
                        UsiaKehamilan = usiaKehamilan,
                        HariPerkiraanLahir = hariPerkiraanLahir,
                        HamilKe = hamilKe
                    )

                    dataList.add(dataItem)
                }

                if (dataList.isEmpty()) {
                    // Jika dataList kosong, tampilkan notifikasi "Belum ada data untuk ID Bidan yang diinputkan"
                    Toast.makeText(context, "Belum ada data untuk ID Bidan yang diinputkan", Toast.LENGTH_SHORT).show()
                } else {
                    originalDataFromAPI = dataList.toList() // Salin data dari dataList ke originalDataFromAPI
                    notifyDataSetChanged()
                }
            },
            { error ->
                Toast.makeText(context, "Terjadi kesalahan: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(stringRequest)
    }

    fun getOriginalDataFromAPI(): List<data> {
        return originalDataFromAPI
    }
}
