package com.example.tugasakhir2


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class data2(
    var trimester: String,
    var beratbadan: String,
    var tinggiBadan: String,
    var lila: String,
    var imt: String,
    var hb: String,
    var goldarah: String,
    var tensidarah: String,
    var rot: String,
    var map: String,
    var pendeteksifaktorrisiko: String,
    var skorrisiko: String,
    var jarakKehamilan: String,
    var statusimunisasiTT: String,
    var pemberianimunisasi: String
) : Parcelable

