package com.ana.labpro.ui.registro

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ana.labpro.data.ResourceRemote
import com.ana.labpro.data.UserRepository
import com.ana.labpro.model.User
import emailValidator
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    val banRegister : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    private val userRepository = UserRepository()

    private val _errorMsg: MutableLiveData<String?> = MutableLiveData()
    val errorMsg: LiveData<String?> = _errorMsg

    private val _registerSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val registerSuccess: LiveData<Boolean> = _registerSuccess

    fun validateData(email: String, password: String, repeatPassword: String) {
        if (email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()){
            _errorMsg.value = "Debe escribir todos los datos de registro"
            banRegister.value = false
        } else if (password != repeatPassword) {
            _errorMsg.value = "Las contraseñas no coinciden"
            banRegister.value = false
        } else {
            if (password.length < 6){
                _errorMsg.value = "Las contraseña debe tener mínimo 6 dígitos"
                //banRegister.value = true
            }
            else{
                if(!emailValidator(email)){
                    _errorMsg.value = "El correo electrónico está mal escrito, revise su formato"
                }
                else {
                    viewModelScope.launch {
                        var result = userRepository.registerUser(email, password)
                        result.let { resourceRemote ->
                            when (resourceRemote){
                                is ResourceRemote.Success -> {
                                    var uid = result.data
                                    uid?.let { Log.d("uid User", it) }
                                    val user = User(uid, email)
                                    createUser(user)
                                    //_registerSuccess.postValue(true)
                                    //_errorMsg.postValue("Usuario creado exitosamente")
                                    //banRegister.value = true
                                }
                                is ResourceRemote.Error -> {
                                    var msg = result.message
                                    when(msg){
                                       "The email address is already in use by another account."-> msg = "Ya existe una cuenta con ese correo electrónico"
                                        "A network error (such as timeout, interrupted connection or unreachable host) has occurred." -> msg = "Revise su conexión de red"
                                    }
                                    _errorMsg.postValue(msg)
                                }
                                else -> {
                                    //don't use
                                }
                            }
                        }
                    }

                }
            }

        }
    }

    private fun createUser(user: User) {
        viewModelScope.launch {
            val result = userRepository.createUser(user)
            result.let {resourceRemote ->
                when(resourceRemote){
                    is ResourceRemote.Success ->{
                        _registerSuccess.postValue(true)
                        _errorMsg.postValue("Usuario creado exitosamente")
                    }
                    is ResourceRemote.Error ->{
                        var msg = result.message
                        _errorMsg.postValue(msg)
                    }
                    else ->{
                        //don't use
                    }
                }

            }
        }

    }
}
