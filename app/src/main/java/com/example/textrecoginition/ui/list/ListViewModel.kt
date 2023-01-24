package com.example.textrecoginition.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.textrecoginition.domain.usecase.ITextUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val useCase: ITextUseCase) : ViewModel() {
    fun getAllText() = useCase.getAllText().asLiveData()
}