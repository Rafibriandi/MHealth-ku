package com.example.tugasakhir2


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class data3(
    var trimester: String,
    var beratbadan: String,
    var lila: String,
    var imt: String,
    var hb: String,
    var tensidarah: String,
    var rot: String,
    var map: String,
    var skorrisiko: String,
    var statusimunisasiTT: String,
    var pemberianimunisasi: String
) : Parcelable
