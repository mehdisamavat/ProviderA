package com.example.providerA.di


import com.example.data.local.dao.UserDao
import com.example.data.repository.UserRepository
import com.example.domain.repository.IUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProjectRepository(userDao: UserDao): IUserRepository = UserRepository(userDao)


}