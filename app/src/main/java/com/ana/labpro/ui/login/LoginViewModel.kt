package com.ana.labpro.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ana.labpro.data.ResourceRemote
import com.ana.labpro.data.UserRepository
import emailValidator
import kotlinx.coroutines.launch


class LoginViewModel: ViewModel() {

    private val userRepository = UserRepository()

    private val _registerSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val registerSuccess: LiveData<Boolean> = _registerSuccess

    private val _userLoggedIn: MutableLiveData<Boolean> = MutableLiveData()
    val userLoggedIn: LiveData<Boolean> = _userLoggedIn

    fun validateData(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()){
            _errorMsg.value = "Debe digitar todos los campos"
            //banLogin.value = false
        }else {
            if (password.length < 6){
                _errorMsg.value = "Las contraseña debe tener mínimo 6 dígitos"
                //banRegister.value = true
            } else {
                if(!emailValidator(email)){
                    _errorMsg.value = "El correo electrónico está mal escrito, revise su formato"
                } else {
                    viewModelScope.launch {
                        val result = userRepository.loginUser(email,password)
                        result.let { resourceRemote ->
                            when (resourceRemote){
                                is ResourceRemote.Success -> {
                                    _registerSuccess.postValue(true)
                                    _errorMsg.postValue("Bienvenido")
                                    banLogin.value = true
                                    //banRegister.value = true
                                }
                                is ResourceRemote.Error -> {
                                    var msg = result.message
                                    when(msg){
                                        "The email address is already in use by another account."-> msg = "Ya existe una cuenta con ese correo electrónico"
                                        "A network error (such as timeout, interrupted connection or unreachable host) has occurred." -> msg = "Revise su conexión de red"
                                        "An internal error has occurred. [ INVALID_LOGIN_CREDENTIALS ]" -> msg = "Correo electrónico o contraseña inválida"
                                    }
                                    _errorMsg.postValue(msg!!)
                                }
                                else -> {
                                    //don't use
                                }
                            }
                        }
                    }
                }

            }
        }//banLogin.value = true


    }

    fun verifyUser() {
        viewModelScope.launch {
            val result = userRepository.verifyUser()
            result.let { resourceRemote ->
                when (resourceRemote) {
                    is ResourceRemote.Success -> {
                        if (result.data == false) {
                            _userLoggedIn.postValue(true)
                            Log.d("LoginViewModel", "Usuario verificado")
                        }
                    }
                    is ResourceRemote.Error -> {
                        var msg = result.message
                        when (msg) {
                            "A network error (such as timeout, interrupted connection or unreachable host) has occurred." -> {
                                msg = "Revise su conexión de red al verificar el usuario"
                                Log.d("LoginViewModel", "Error de red al verificar el usuario")
                            }
                            "An internal error has occurred." -> {
                                msg = "Error interno al verificar el usuario"
                                Log.d("LoginViewModel", "Error interno al verificar el usuario")
                            }
                            else -> {
                                // Puedes agregar más casos según sea necesario
                            }
                        }
                        _errorMsg.postValue(msg!!)
                    }
                    else -> {
                        // No uses este caso si no es necesario
                    }
                }
            }
        }
    }

    val banLogin : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val _errorMsg: MutableLiveData<String> = MutableLiveData()
    val errorMsg: LiveData<String> = _errorMsg

    /*
    private val _errorMsg: MutableLiveData<String?> = MutableLiveData()
    val errorMsg: LiveData<String?> = _errorMsg
     */


}
