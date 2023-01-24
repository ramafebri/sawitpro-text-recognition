package com.example.textrecoginition.domain.usecase

import com.example.textrecoginition.domain.Resource
import com.example.textrecoginition.domain.repository.ITextRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TextUseCase @Inject constructor(private val repository: ITextRepository) :
    ITextUseCase {
    override fun postText(text: String): Flow<Resource<String>> {
        return repository.postText(text)
    }

    override fun getAllText(): Flow<Resource<List<String>>> {
        return repository.getAllText()
    }
}