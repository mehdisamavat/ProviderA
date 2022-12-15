package com.example.data.localdatasource

import android.net.Uri
import com.example.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IUserLocalDataSource {

    fun getUsers(): Flow<List<User?>>
    fun getUser(id: Int): Flow<User?>
    fun insertUser(user: User): Uri?
    fun deleteUser(id: Int): Int
    fun updateUser(user: User): Int

}

