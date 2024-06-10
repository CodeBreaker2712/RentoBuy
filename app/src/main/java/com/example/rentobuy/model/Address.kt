package com.example.rentobuy.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    val userId: String = "",
    val addressLabel: String = "",
    val address: String = "",
    val zipCode: String = "",
    val lat: Double = 0.0,
    val long: Double = 0.0,
    val name: String = "",
    var id: String = "",
) : Parcelable