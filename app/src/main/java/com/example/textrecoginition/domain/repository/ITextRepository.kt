package com.example.textrecoginition.domain.repository

import com.example.textrecoginition.domain.Resource
import kotlinx.coroutines.flow.Flow

interface ITextRepository {
    fun postText(text: String): Flow<Resource<String>>
    fun getAllText(): Flow<Resource<List<String>>>
}