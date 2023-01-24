package com.example.textrecoginition.data.remote

import com.example.textrecoginition.data.response.ApiResponse
import kotlinx.coroutines.flow.Flow

interface ITextRemoteDataSource {
    fun postText(text: String): Flow<ApiResponse<String>>
    fun getAllText(): Flow<ApiResponse<List<String>>>
    fun getTextById(id: String): Flow<ApiResponse<String>>
}