package com.example.rentobuy.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WishListModel(
    val user_id: String = "",
    val product_owner_id: String = "",
    val product_id: String = "",
    val title: String = "",
    val price: String = "",
    val image: String = "",
    var id: String = "",
) : Parcelable