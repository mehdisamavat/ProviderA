package com.example.providerA.di


import com.example.domain.repository.IUserRepository
import com.example.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideInsertUserUseCase(iUserRepository: IUserRepository) =
        InsertUserUseCase(iUserRepository)





}