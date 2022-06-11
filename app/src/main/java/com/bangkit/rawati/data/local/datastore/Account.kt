package com.bangkit.rawati.data.local.datastore

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Account(
    val user_id: String,
    val token: String,
    val isLogin: Boolean
): Parcelable
