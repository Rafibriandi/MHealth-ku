package com.example.tugasakhir2


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class data(
    var NIK: String,
    var IDPasien: String,
    var NamaPasien: String,
    var NamaSuami: String,
    var Alamat: String,
    var NoTelepon: String,
    var UmurPasien: String,
    var HPHT: String,
    var UsiaKehamilan: String,
    var HariPerkiraanLahir: String,
    var HamilKe: String
) : Parcelable

