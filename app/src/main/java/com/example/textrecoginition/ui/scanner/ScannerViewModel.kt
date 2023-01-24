package com.example.textrecoginition.ui.scanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.textrecoginition.domain.Resource
import com.example.textrecoginition.domain.usecase.ITextUseCase
import com.google.mlkit.vision.text.Text
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(private val useCase: ITextUseCase) : ViewModel() {
    fun postText(text: Text): LiveData<Resource<String>> {
        val stringText = StringBuilder()
        for (block in text.textBlocks) {
            stringText.append(block.text)
            stringText.append(SPACE)
        }
        return useCase.postText(stringText.toString()).asLiveData()
    }

    companion object {
        const val SPACE = " "
    }
}