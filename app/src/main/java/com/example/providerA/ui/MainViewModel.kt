package com.example.providerA.ui

import androidx.lifecycle.*
import com.example.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val insertUserUseCase: InsertUserUseCase,
) : ViewModel() {



    fun insertUser(name: String, checked: Boolean) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                insertUserUseCase.invoke(name = name, checked = checked)
//            } catch (e: Exception) {
//                stateResponse.postValue(e.message)
//            }
//        }
    }



}