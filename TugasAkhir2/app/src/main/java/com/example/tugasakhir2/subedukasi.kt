package com.example.tugasakhir2
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tugasakhir2.databinding.PengukuranpasiendetailBinding
import com.example.tugasakhir2.databinding.SubedukasiBinding

class SubEdukasiActivity : AppCompatActivity() {
    var b: Bundle? = null
    private lateinit var binding: SubedukasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.subedukasi)
        binding = SubedukasiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initview()


}
    private fun initview() {
        b = intent.extras
        binding.tulisanedukasi.text = b?.getString("isi") ?: "Data tidak tersedia"

// Set gambar pada ImageView jika ada
        val gambarByteArray = b?.getByteArray("gambar")
        if (gambarByteArray != null) {
            val bitmap = BitmapFactory.decodeByteArray(gambarByteArray, 0, gambarByteArray.size)
            binding.gambaredukasi.setImageBitmap(bitmap)
        } else {
            // Set gambar placeholder jika tidak ada gambar
            binding.gambaredukasi.setImageResource(R.drawable.gambaredukasi)
        }
    }
}








