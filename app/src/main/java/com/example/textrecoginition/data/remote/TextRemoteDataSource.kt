package com.example.textrecoginition.data.remote

import com.example.textrecoginition.data.response.ApiResponse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TextRemoteDataSource @Inject constructor(private val firestore: FirebaseFirestore) :
    ITextRemoteDataSource {
    override fun postText(text: String): Flow<ApiResponse<String>> {
        return flow {
            val task = firestore.collection(DATABASE).add(hashMapOf(FIELD to text))
            try {
                val res = task.await()
                getTextById(res.id).collect {
                    emit(it)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(Throwable(e.message)))
            }
        }
    }

    override fun getAllText(): Flow<ApiResponse<List<String>>> {
        return flow {
            val task = firestore.collection(DATABASE).get()
            try {
                val res = task.await()
                val textList = mutableListOf<String>()
                for (item in res.documents) {
                    val value = item.get(FIELD)
                    textList.add(value.toString())
                }
                emit(ApiResponse.Success(textList))
            } catch (e: Exception) {
                emit(ApiResponse.Error(Throwable(e.message)))
            }
        }
    }

    override fun getTextById(id: String): Flow<ApiResponse<String>> {
        return flow {
            val task = firestore.collection(DATABASE).document(id).get()
            try {
                val res = task.await()
                val value = res.get(FIELD)
                emit(ApiResponse.Success(value.toString()))
            } catch (e: Exception) {
                emit(ApiResponse.Error(Throwable(e.message)))
            }
        }
    }

    companion object {
        private const val DATABASE = "texts"
        private const val FIELD = "item"
    }
}