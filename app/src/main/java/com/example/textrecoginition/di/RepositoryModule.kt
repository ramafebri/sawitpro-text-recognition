package com.example.textrecoginition.di

import com.example.textrecoginition.data.TextRepository
import com.example.textrecoginition.domain.repository.ITextRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [FirebaseModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideRepository(repository: TextRepository): ITextRepository
}