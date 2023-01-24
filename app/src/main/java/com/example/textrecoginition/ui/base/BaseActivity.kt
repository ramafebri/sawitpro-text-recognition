package com.example.textrecoginition.ui.base

import android.R.id.home
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VBinding : ViewBinding> : AppCompatActivity() {
    lateinit var binding: VBinding
    protected abstract fun getViewBinding(): VBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
    }

    fun showBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            home -> {
                try {
                    NavUtils.navigateUpFromSameTask(this)
                } catch (e: Exception) {
                    finish()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}