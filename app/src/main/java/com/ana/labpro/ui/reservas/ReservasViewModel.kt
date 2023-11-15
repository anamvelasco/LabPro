package com.ana.labpro.ui.reservas

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ana.labpro.data.ReservasRepository
import com.ana.labpro.model.Reservas
import androidx.lifecycle.viewModelScope
import com.ana.labpro.data.ResourceRemote
import com.ana.labpro.model.User
import kotlinx.coroutines.launch

class ReservasViewModel : ViewModel() {

    val reservasRepository = ReservasRepository()

    private val _errorMsg: MutableLiveData<String?> = MutableLiveData()
    val errorMsg: LiveData<String?> = _errorMsg

    private val _createReservaSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val createReservaSuccess: LiveData<Boolean> = _createReservaSuccess

    fun validateFields(name: String, cedula: Int, email: String, programa: String, maquina: String, date: String, hour: String) {
        if (name.isEmpty() || email.isEmpty() || programa.isEmpty() || maquina.isEmpty()) {
            _errorMsg.value = "Debe digitar los campos"
        } else {
            val reserva = Reservas(name = name, cedula = cedula, email = email, programa = programa, maquina = maquina, date = date, hour = hour)
            viewModelScope.launch {
                var result = reservasRepository.createReserva(reserva)
                result.let { resourceRemote ->
                    when (resourceRemote) {
                        is ResourceRemote.Success -> {
                            _errorMsg.postValue("Reserva almacenada con éxito")
                            _createReservaSuccess.postValue(true)
                        }
                        is ResourceRemote.Error -> {
                            var msg = result.message
                            _errorMsg.postValue(msg)
                        }
                        else -> {
                            // don't use
                        }
                    }
                }
            }
        }
    }
}
