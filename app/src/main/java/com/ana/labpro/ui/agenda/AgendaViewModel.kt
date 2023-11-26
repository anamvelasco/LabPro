package com.ana.labpro.ui.agenda

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ana.labpro.data.ReservasRepository
import com.ana.labpro.data.ResourceRemote
import com.ana.labpro.model.Reservas
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.launch

class AgendaViewModel: ViewModel() {
    private val reservasRepository = ReservasRepository()
    private var reservasListLocal = mutableListOf<Reservas?>()

    private val _errorMsg : MutableLiveData<String?> = MutableLiveData()
    val errorMsg: LiveData<String?> = _errorMsg

    private val _reservaErased : MutableLiveData<Boolean> = MutableLiveData()
    val reservaErased : LiveData<Boolean> = _reservaErased

    private val _reservasList: MutableLiveData<MutableList<Reservas?>> = MutableLiveData()
    val reservasList: LiveData<MutableList<Reservas?>> = _reservasList

    fun loadReservasbyDate(fecha: String) {
        reservasListLocal.clear()
        viewModelScope.launch {
            val result = reservasRepository.loadReservasbyDate(fecha)
            result.let { resourceRemote ->
                when(resourceRemote){
                    is ResourceRemote.Success ->{
                        result.data?.documents?.forEach{document ->
                            val reserva = document.toObject<Reservas>()
                            reservasListLocal.add(reserva)
                        }
                        _reservasList.postValue(reservasListLocal)
                    }
                    is ResourceRemote.Error -> {
                        val msg = result.message
                        _errorMsg.postValue(msg)

                    }
                    else -> {
                        //don't use
                    }
                }
            }

        }

    }

    fun deleteReservaConValidacionDeRol(reserva: Reservas?) {
        viewModelScope.launch {
            val result = reservasRepository.deleteReservaConValidacionDeRol(reserva)
            result.let { resourceRemote ->
                when (resourceRemote) {
                    is ResourceRemote.Success -> {
                        _reservaErased.postValue(true)
                        _errorMsg.postValue("Reserva eliminada con éxito")
                    }
                    is ResourceRemote.Error -> {
                        val msg = result.message
                        _errorMsg.postValue(msg)
                    }
                    else -> {
                        // no se utiliza
                    }
                }
            }
        }
    }

}