package com.example.textrecoginition.ui.home

import android.os.Bundle
import com.example.textrecoginition.databinding.ActivityMainBinding
import com.example.textrecoginition.extension.navigateTo
import com.example.textrecoginition.ui.list.ListActivity
import com.example.textrecoginition.ui.scanner.ScannerActivity
import com.example.textrecoginition.ui.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
    }

    private fun initListener() {
        with(binding) {
            buttonMoveToList.setOnClickListener {
                navigateTo(ListActivity::class.java)
            }
            buttonMoveToScanner.setOnClickListener {
                navigateTo(ScannerActivity::class.java)
            }
        }
    }
}