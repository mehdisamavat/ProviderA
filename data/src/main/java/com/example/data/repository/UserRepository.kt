package com.example.data.repository


import com.example.data.entity.UserEntity
import com.example.data.local.dao.UserDao
import com.example.domain.repository.IUserRepository

class UserRepository(private val userDao: UserDao) : IUserRepository {
    override fun insertUser(name: String, checked: Boolean): Long {
        return userDao.insert(UserEntity(0, name, checked))
    }


}