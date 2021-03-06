package com.infigo.watchsaleapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartItem(
    val cat_id: String?="",
    val id: String?="",
    val img: String?="",
    val name: String?="",
    val offer: String?="",
    val price: String?="",
    val quantity: Int?=0
):Parcelable