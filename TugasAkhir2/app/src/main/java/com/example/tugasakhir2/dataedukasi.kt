package com.example.tugasakhir2

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class dataedukasi(
    var judul: String,
    var isi: String,
    var gambar: ByteArray // Tipe data Blob untuk menyimpan gambar
) : Parcelable
