package com.example.textrecoginition.domain.usecase

import com.example.textrecoginition.domain.Resource
import kotlinx.coroutines.flow.Flow

interface ITextUseCase {
    fun postText(text: String): Flow<Resource<String>>
    fun getAllText(): Flow<Resource<List<String>>>
}