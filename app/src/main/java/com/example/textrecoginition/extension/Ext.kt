package com.example.textrecoginition.extension

import android.util.Log
import android.view.View
import androidx.viewbinding.BuildConfig

fun logError(tag: String, message: String, exception: Exception) {
    if (BuildConfig.DEBUG) {
        Log.e(tag, message, exception)
    }
}

fun View.showOrHide(isShow: Boolean) {
    this.visibility = if (isShow) View.VISIBLE else View.GONE
}