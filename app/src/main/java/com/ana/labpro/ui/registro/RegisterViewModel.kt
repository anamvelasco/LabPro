package com.ana.labpro.ui.registro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.ana.labpro.R
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class RegisterViewModel : ViewModel() {
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