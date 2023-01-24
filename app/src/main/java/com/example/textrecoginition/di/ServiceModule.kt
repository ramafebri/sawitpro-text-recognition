package com.example.textrecoginition.di

import com.example.textrecoginition.data.remote.ITextRemoteDataSource
import com.example.textrecoginition.data.remote.TextRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [FirebaseModule::class])
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun provideRemoteSource(remoteSource: TextRemoteDataSource): ITextRemoteDataSource
}