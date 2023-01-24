package com.example.textrecoginition.extension

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

fun Activity.navigateTo(destination: Class<out AppCompatActivity>) {
    startActivity(Intent(this, destination))
}

fun Activity.navigateTo(destination: Class<out AppCompatActivity>, bundle: Bundle) {
    startActivity(Intent(this, destination).apply {
        putExtras(bundle)
    })
}

fun Activity.showToast(message: String) {
    Toast.makeText(
        this.applicationContext,
        message,
        Toast.LENGTH_SHORT
    ).show()
}