package com.example.adapter
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tugasakhir2.R
import com.example.tugasakhir2.SubEdukasiActivity
import com.example.tugasakhir2.dataedukasi
import org.json.JSONArray
import org.json.JSONObject

class EdukasiAdapter(private val context: Context) : RecyclerView.Adapter<EdukasiAdapter.EdukasiViewHolder>() {
    private val dataList: MutableList<dataedukasi> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EdukasiViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.isirecycleedukasi, parent, false)
        return EdukasiViewHolder(view)
    }

    override fun onBindViewHolder(holder: EdukasiViewHolder, position: Int) {
        holder.bindModel(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class EdukasiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val judulEdukasi: TextView = itemView.findViewById(R.id.juduledukasi)
        private val cv: CardView = itemView.findViewById(R.id.cv)
        private val gambaredukasi: ImageView = itemView.findViewById(R.id.gambaredukasi)

        fun bindModel(edukasi: dataedukasi) {
            cv.setOnClickListener {
                val intent = Intent(context, SubEdukasiActivity::class.java).apply {
                    putExtra("judul", edukasi.judul)
                    putExtra("isi", edukasi.isi)
                    putExtra("gambar",edukasi.gambar)
                }
                context.startActivity(intent)
            }

            judulEdukasi.text = edukasi.judul
            if (edukasi.gambar != null) {
                val bitmap = BitmapFactory.decodeByteArray(edukasi.gambar, 0, edukasi.gambar.size)
                gambaredukasi.setImageBitmap(bitmap)
            }
        }
    }

    fun loadFromAPI2() {
        val url = "https://18319028tugasakhir.com/getmateriedukasipasien.php"

        val requestQueue = Volley.newRequestQueue(context)

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val serverResponse = jsonObject.getJSONArray("server_response")
                    dataList.clear()
                    for (i in 0 until serverResponse.length()) {
                        val item = serverResponse.getJSONObject(i)
                        val gambarString = item.getString("Gambar")
                        val gambar: ByteArray? = gambarString?.let { Base64.decode(it, Base64.DEFAULT) }
                        val dataItem = gambar?.let {
                            dataedukasi(
                                judul = item.getString("Judul"),
                                isi = item.getString("Isi"),
                                gambar = it
                                // Tambahkan atribut lainnya sesuai dengan data JSON Anda
                            )
                        }
                        if (dataItem != null) {
                            dataList.add(dataItem)
                        }
                    }
                    notifyDataSetChanged()
                } catch (e: Exception) {
                    e.printStackTrace()
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
