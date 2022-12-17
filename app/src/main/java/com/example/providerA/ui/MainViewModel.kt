package com.example.providerA.ui

import androidx.lifecycle.*
import com.example.domain.model.User
import com.example.domain.usecase.*
import com.example.providerA.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    getUsersUseCase: GetUsersUseCase,
    private val insertUserUseCase: InsertUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
) : ViewModel() {

    val stateResponse = SingleLiveEvent<String>()

    val allUsers: LiveData<List<User?>> = getUsersUseCase().asLiveData()

    fun insertUser(name: String, checked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                insertUserUseCase.invoke(name = name, checked = checked)
            } catch (e: Exception) {
                stateResponse.postValue(e.message)
            }
        }
    }

    fun deleteUser(id: Int) {
        viewModelScope.launch {
            try {
                deleteUserUseCase.invoke(id)
            } catch (e: Exception) {
                stateResponse.postValue(e.message)
            }

        }
    }

    fun updateUser(id: Int, name: String, checked: Boolean) {
        viewModelScope.launch {
            try {
                updateUserUseCase.invoke(id, name, checked)
            } catch (e: Exception) {
                stateResponse.postValue(e.message)
            }

        }
    }


}