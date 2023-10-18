package com.ana.labpro.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {
    fun validateData(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()){
            _errorMsg.value = "Debe escribir los datos de login"
            banLogin.value = false
        }else{
            banLogin.value = true
        }

    }

    val banLogin : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val _errorMsg: MutableLiveData<String> = MutableLiveData()
    val errorMsg: LiveData<String> = _errorMsg



}