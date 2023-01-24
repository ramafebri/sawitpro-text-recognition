package com.example.textrecoginition.data

import com.example.textrecoginition.data.remote.ITextRemoteDataSource
import com.example.textrecoginition.data.response.ApiResponse
import com.example.textrecoginition.domain.Resource
import com.example.textrecoginition.domain.repository.ITextRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TextRepository @Inject constructor(private val remoteDataSource: ITextRemoteDataSource) :
    ITextRepository {
    override fun postText(text: String): Flow<Resource<String>> {
        return callService {
            remoteDataSource.postText(text)
        }
    }

    override fun getAllText(): Flow<Resource<List<String>>> {
        return callService {
            remoteDataSource.getAllText()
        }
    }

    private fun <T> callService(
        apiFunction: suspend () -> Flow<ApiResponse<T>>
    ): Flow<Resource<T>> {
        return flow {
            emit(Resource.Loading())
            when (val response = apiFunction.invoke().single()) {
                is ApiResponse.Success -> {
                    emit(
                        Resource.Success(
                            response.data
                        )
                    )
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(response.throwable.message.orEmpty()))
                }
            }
        }
    }
}