package com.example.textrecoginition.di

import com.example.textrecoginition.domain.usecase.ITextUseCase
import com.example.textrecoginition.domain.usecase.TextUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {
    @Binds
    @ViewModelScoped
    abstract fun provideUseCase(textUseCase: TextUseCase): ITextUseCase
}