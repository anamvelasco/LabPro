package com.ana.labpro.ui.login

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
        }//banLogin.value = true


    }

    val banLogin : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val _errorMsg: MutableLiveData<String> = MutableLiveData()
    val errorMsg: LiveData<String> = _errorMsg



}