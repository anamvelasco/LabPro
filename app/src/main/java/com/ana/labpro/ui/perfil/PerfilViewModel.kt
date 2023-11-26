// PerfilViewModel.kt
package com.ana.labpro.ui.perfil

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ana.labpro.data.ResourceRemote
import com.ana.labpro.data.UserRepository
import com.ana.labpro.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class PerfilViewModel : ViewModel() {
    private val userRepository = UserRepository()

    private val _errorMsg: MutableLiveData<String?> = MutableLiveData()
    val errorMsg: LiveData<String?> = _errorMsg

    private val _userData: MutableLiveData<User?> = MutableLiveData()
    val userData: LiveData<User?> = _userData

    private val _userLoggedOut: MutableLiveData<Boolean> = MutableLiveData()
    val userLoggedOut: LiveData<Boolean> = _userLoggedOut

    fun loadCurrentUser() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { uid ->
            Log.d("PerfilViewModel", "UID del usuario: $uid")
            loadUser(uid)
        } ?: run {
            _errorMsg.postValue("El usuario actual no tiene un UID válido.")
        }
    }

    private fun loadUser(uid: String) {
        viewModelScope.launch {
            val result = userRepository.loadUser(uid)
            result.let { resourceRemote ->
                when (resourceRemote) {
                    is ResourceRemote.Success -> {
                        result.data?.let { user ->
                            Log.d("PerfilViewModel", "Datos del usuario después de la actualización: $user")
                            _userData.postValue(user)
                        }
                    }
                    is ResourceRemote.Error -> {
                        val msg = result.message
                        _errorMsg.postValue(msg)
                        Log.e("PerfilViewModel", "Error al cargar usuario después de la actualización: $msg")
                    }
                    else -> {
                        Log.e("PerfilViewModel", "Error inesperado al cargar usuario después de la actualización")
                    }
                }
            }
        }
    }


    fun actualizarUsuario(user: User) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { uid ->
            Log.d("PerfilViewModel", "Actualizando usuario con UID: $uid, Datos: $user") // Agregar esta línea
            viewModelScope.launch {
                // Asegúrate de conservar uid durante la actualización
                val updatedUser = user.copy(uid = uid)
                Log.d("PerfilViewModel", "Usuario actualizado antes de la operación Firestore: $updatedUser") // Agregar esta línea
                val result = userRepository.actualizarUsuario(updatedUser, uid)
                result.let { resourceRemote ->
                    when (resourceRemote) {
                        is ResourceRemote.Success -> {
                            Log.d("PerfilViewModel", "Usuario actualizado correctamente") // Agregar esta línea
                            // Intenta cargar el usuario después de la actualización
                            loadUser(uid)
                        }
                        is ResourceRemote.Error -> {
                            val msg = result.message
                            _errorMsg.postValue(msg)
                            Log.e("PerfilViewModel", "Error al actualizar usuario: $msg")
                        }
                        else -> {
                            Log.e("PerfilViewModel", "Error inesperado al actualizar usuario")
                        }
                    }
                }
            }
        } ?: run {
            _errorMsg.postValue("El usuario actual no tiene un UID válido al intentar actualizar.")
        }
    }

    fun signOut() {
        viewModelScope.launch {
            val result = userRepository.signOut()
            result.let { resourceRemote ->
                when (resourceRemote) {
                    is ResourceRemote.Success -> {
                        if (result.data == true) {
                            _userLoggedOut.postValue(true)
                        }
                    }

                    is ResourceRemote.Error -> {
                        var msg = result.message
                        when (msg) {
                            "A network error (such as timeout, interrupted connection or unreachable host) has occurred." -> {
                                msg = "Revise su conexión de red al verificar el usuario"
                            }

                            "An internal error has occurred." -> {
                                msg = "Error interno al verificar el usuario"
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


}
