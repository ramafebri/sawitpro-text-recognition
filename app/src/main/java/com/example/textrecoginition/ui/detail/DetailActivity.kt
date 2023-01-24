package com.example.textrecoginition.ui.detail

import android.os.Bundle
import com.example.textrecoginition.databinding.ActivityDetailBinding
import com.example.textrecoginition.ui.base.BaseActivity

class DetailActivity : BaseActivity<ActivityDetailBinding>() {

    override fun getViewBinding(): ActivityDetailBinding {
        return ActivityDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showBackButton()
        initData()
    }

    private fun initData() {
        binding.editText.setText(intent.getStringExtra(EXTRA_DATA))
    }

    companion object {
        const val EXTRA_DATA = "EXTRA_DATA"
    }
}