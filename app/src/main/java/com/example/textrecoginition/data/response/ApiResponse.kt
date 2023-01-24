package com.example.textrecoginition.data.response

sealed class ApiResponse<out R> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val throwable: Throwable) : ApiResponse<Nothing>()
}