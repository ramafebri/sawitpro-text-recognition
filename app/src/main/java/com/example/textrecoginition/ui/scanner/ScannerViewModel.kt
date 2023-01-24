package com.example.textrecoginition.ui.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.textrecoginition.domain.usecase.ITextUseCase
import com.google.mlkit.vision.text.Text
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(private val useCase: ITextUseCase) : ViewModel() {
    fun postText(text: Text) = useCase.postText(
        text = text.textBlocks.joinToString(separator = SPACE) {
            it.text
        }
    ).asLiveData()

    companion object {
        const val SPACE = " "
    }
}