package com.example.domain.usecase

import com.example.domain.repository.IUserRepository


class InsertUserUseCase(private val iUserRepository: IUserRepository) {
    operator fun invoke(name: String, checked: Boolean): Long {
        return iUserRepository.insertUser(name, checked)
    }
}