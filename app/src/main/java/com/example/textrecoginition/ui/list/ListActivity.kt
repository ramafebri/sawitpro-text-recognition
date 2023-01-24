package com.example.textrecoginition.ui.list

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import com.example.textrecoginition.databinding.ActivityListBinding
import com.example.textrecoginition.domain.Resource
import com.example.textrecoginition.extension.navigateTo
import com.example.textrecoginition.extension.showOrHide
import com.example.textrecoginition.extension.showToast
import com.example.textrecoginition.ui.base.BaseActivity
import com.example.textrecoginition.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListActivity : BaseActivity<ActivityListBinding>() {
    private val viewModel: ListViewModel by viewModels()
    private lateinit var listAdapter: ListAdapter

    override fun getViewBinding(): ActivityListBinding {
        return ActivityListBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showBackButton()
        getAllText()
    }

    private fun getAllText() {
        viewModel.getAllText().observe(this) { res ->
            when (res) {
                is Resource.Success -> {
                    setLoadingVisibility(false)
                    if (res.data != null) {
                        initRecyclerView(res.data)
                    }
                }
                is Resource.Error -> {
                    setLoadingVisibility(false)
                    showToast(res.message.toString())
                }
                is Resource.Loading -> {
                    setLoadingVisibility(true)
                }
            }
        }
    }

    private fun initRecyclerView(data: List<String>) {
        listAdapter = ListAdapter(data, ::onItemClicked)
        binding.recyclerView.adapter = listAdapter
    }

    private fun onItemClicked(text: String) {
        navigateTo(
            DetailActivity::class.java,
            bundleOf(DetailActivity.EXTRA_DATA to text)
        )
    }

    private fun setLoadingVisibility(isShow: Boolean) {
        binding.progressBar.showOrHide(isShow)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.recyclerView.adapter = null
    }
}