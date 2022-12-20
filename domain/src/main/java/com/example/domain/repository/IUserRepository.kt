package com.example.domain.repository

interface IUserRepository {
    fun insertUser(name: String, checked: Boolean): Long

}
