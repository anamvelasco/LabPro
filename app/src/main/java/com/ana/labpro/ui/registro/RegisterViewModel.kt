package com.ana.labpro.ui.registro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {

    val banRegister : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    private val _errorMsg: MutableLiveData<String> = MutableLiveData()
    val errorMsg: LiveData<String> = _errorMsg

    fun validateData(email: String, password: String, repeatPassword: String) {
        if (email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()){
            _errorMsg.value = "Debe escribir todos los datos de registro"
            banRegister.value = false
        } else if (password != repeatPassword) {
            _errorMsg.value = "Las contraseñas no coinciden"
            banRegister.value = false
        } else {
            banRegister.value = true
        }
    }
}
