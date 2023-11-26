package com.ana.labpro.ui.reservas

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ana.labpro.data.ReservasRepository
import com.ana.labpro.model.Reservas
import androidx.lifecycle.viewModelScope
import com.ana.labpro.data.ResourceRemote
import com.ana.labpro.data.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.launch

class ReservasViewModel : ViewModel() {

    val userRepository = UserRepository()
    val reservasRepository = ReservasRepository()

    private val _errorMsg: MutableLiveData<String?> = MutableLiveData()
    val errorMsg: LiveData<String?> = _errorMsg

    private val _createReservaSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val createReservaSuccess: LiveData<Boolean> = _createReservaSuccess

    private val _reservasList: MutableLiveData<List<Reservas>> = MutableLiveData()
    val reservasList: LiveData<List<Reservas>> = _reservasList

    private val _newReserva: MutableLiveData<Reservas> = MutableLiveData()
    val newReserva: LiveData<Reservas> = _newReserva



    suspend fun validateFields(
        name: String,
        cedula: Int,
        email: String,
        programa: String,
        maquina: String,
        date: String,
        hour: String
    ) {
        if (name.isEmpty() || email.isEmpty() || programa.isEmpty() || maquina.isEmpty()) {
            _errorMsg.value = "Debe digitar los campos"
        } else {
            val reserva = Reservas(
                name = name,
                cedula = cedula,
                email = email,
                programa = programa,
                maquina = maquina,
                date = date,
                hour = hour
            )
            viewModelScope.launch {
                var result = reservasRepository.createReserva(reserva)
                result.let { resourceRemote ->
                    when (resourceRemote) {
                        is ResourceRemote.Success -> {
                            _errorMsg.postValue("Reserva almacenada con éxito")
                            _createReservaSuccess.postValue(true)

                            // Incrementar el número de reservas al realizar una reserva
                            resourceRemote.data?.let { uid ->
                                userRepository.incrementarNumReservas(uid)
                                Log.d("ReservasViewModel", "Incremento exitoso de numReservas en Firebase")
                            }

                            // Cargar el usuario después de actualizar las reservas
                            loadCurrentUser()

                            // Después de validar y crear la reserva, actualiza la lista de reservas
                            _newReserva.postValue(reserva)
                        }
                        is ResourceRemote.Error -> {
                            var msg = result.message
                            _errorMsg.postValue(msg)
                        }
                        else -> {
                            // no deberías llegar aquí
                        }
                    }
                }
            }
        }
    }

    suspend fun loadReservasByDateAndHour(date: String, hour: String, maquina: String): ResourceRemote<List<Reservas>> {
        return reservasRepository.loadReservasByDateAndHour(date, hour, maquina)
    }




    fun getReservasByUserEmail(email: String) {
        viewModelScope.launch {
            val result = reservasRepository.getReservasByUserEmail(email)
            result.let { resourceRemote ->
                when (resourceRemote) {
                    is ResourceRemote.Success -> {
                        _reservasList.postValue(result.data ?: emptyList())
                    }

                    is ResourceRemote.Error -> {
                        val msg = result.message
                        _errorMsg.postValue(msg)
                        Log.e("ReservasViewModel", "Error al obtener reservas: $msg")
                    }

                    else -> {
                        Log.e("ReservasViewModel", "Error inesperado al obtener reservas")
                    }
                }
            }
        }
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            val currentUser = FirebaseAuth.getInstance().currentUser
            currentUser?.uid?.let { uid ->
                userRepository.loadUser(uid)
            }
        }
    }

}